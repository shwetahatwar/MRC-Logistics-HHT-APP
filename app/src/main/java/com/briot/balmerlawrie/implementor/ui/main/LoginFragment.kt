package com.briot.balmerlawrie.implementor.ui.main

import android.Manifest
import android.Manifest.permission.READ_PHONE_STATE
import android.content.ContentValues
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.briot.balmerlawrie.implementor.R
import com.briot.balmerlawrie.implementor.UiHelper
import com.briot.balmerlawrie.implementor.repository.local.PrefConstants
import com.briot.balmerlawrie.implementor.repository.local.PrefRepository
import com.briot.balmerlawrie.implementor.repository.remote.SignInResponse
import io.github.pierry.progress.Progress
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.handleCoroutineException


class LoginFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private var progress: Progress? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        username.requestFocus()
        var deviceSerialNumber: String = ""
        try {
            val TelephonyManager = context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (ActivityCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            else {
                Log.d(ContentValues.TAG, "Got Device serial number " + Build.getSerial())
                deviceSerialNumber = Build.getSerial()
            }
        }
        catch ( exception: Throwable ){
            Log.d(ContentValues.TAG, "Getting exception while getting serial number " + exception)
        }

        viewModel.signInResponse.observe(this, Observer<SignInResponse> {
            UiHelper.hideProgress(this.progress)
            this.progress = null

            if (it != null) {
                this.activity?.invalidateOptionsMenu()
                PrefRepository.singleInstance.setKeyValue(PrefConstants().USER_TOKEN, "1")
                PrefRepository.singleInstance.setKeyValue(PrefConstants().id, it.id!!.toString())
                PrefRepository.singleInstance.setKeyValue(PrefConstants().username, it.username!!.toString())
                PrefRepository.singleInstance.setKeyValue(PrefConstants().password, it.password!!)
                PrefRepository.singleInstance.setKeyValue(PrefConstants().deviceId, deviceSerialNumber)
                PrefRepository.singleInstance.setKeyValue(PrefConstants().status, it.status!!.toString())
                PrefRepository.singleInstance.setKeyValue(PrefConstants().USER_ID,"1")

                //Log.d(ContentValues.TAG, "login" + PrefRepository)
                //Log.d(ContentValues.TAG, "context" +   context)


                this.context?.let { it1 -> PrefRepository.singleInstance.serializePrefs(it1) }

                Navigation.findNavController(login).navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                UiHelper.showErrorToast(this.activity as AppCompatActivity, "An error has occurred, please try again.");
            }

        })

        viewModel.networkError.observe(this, Observer<Boolean> {

            if (it == true) {
                UiHelper.hideProgress(this.progress)
                this.progress = null

                var message: String = "Server is not reachable, please check if your network connection is working"
                if (viewModel.errorMessage.isNotEmpty()) {
                    message = viewModel.errorMessage
                }

                UiHelper.showSnackbarMessage(this.activity as AppCompatActivity, message, 3000);
            }
        })


        login.setOnClickListener {
            val keyboard = activity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(activity?.currentFocus?.getWindowToken(), 0)

            // @dineshgajjar - remove following coments later on
            this.progress = UiHelper.showProgressIndicator(this.activity as AppCompatActivity, "Please wait")
            viewModel.loginUser(username.text.toString(), password.text.toString(),deviceSerialNumber)

        }
    }

}
