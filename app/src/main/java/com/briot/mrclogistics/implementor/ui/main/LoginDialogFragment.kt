package com.briot.mrclogistics.implementor.ui.main

import android.app.Dialog
import android.content.ContentValues
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.briot.mrclogistics.implementor.R
import com.briot.mrclogistics.implementor.UiHelper
import com.briot.mrclogistics.implementor.repository.local.PrefConstants
import com.briot.mrclogistics.implementor.repository.local.PrefRepository
import com.briot.mrclogistics.implementor.repository.remote.SignInResponse
import io.github.pierry.progress.Progress
import kotlinx.android.synthetic.main.login_dialog_fragment.*
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.user_profile_fragment.*

class LoginDialogFragment : Fragment() {

    companion object {
        fun newInstance() = LoginDialogFragment()
    }

    private var progress: Progress? = null
    var alertDialog: AlertDialog? = null
    var adminAuthenticated: Boolean = false
    private lateinit var viewModel: LoginDialogViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_dialog_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginDialogViewModel::class.java)

        Log.d(ContentValues.TAG, "LoginDialog.kt --->")
        viewModel.signInResponse.observe(viewLifecycleOwner, Observer<SignInResponse> {
            UiHelper.hideProgress(this.progress)
            this.progress = null

            if (it != null) {

                this.activity?.invalidateOptionsMenu()
                PrefRepository.singleInstance.setKeyValue(PrefConstants().id, it.id!!.toString())
                PrefRepository.singleInstance.setKeyValue(PrefConstants().status, it.status!!.toString())
                PrefRepository.singleInstance.setKeyValue(PrefConstants().USER_ID,"1")

                this.context?.let { it1 -> PrefRepository.singleInstance.serializePrefs(it1) }

               // Navigation.findNavController(login).navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                UiHelper.showErrorToast(this.activity as AppCompatActivity, "An error has occurred, please try again.");
            }
        })

        dialogLoginBtn.setOnClickListener {
            val name = dialogNameEt.text.toString()
            val password = dialogPasswEt.text.toString()

            viewModel.loginUser(name, password,"");
            Log.d(ContentValues.TAG, "name -->"+ name)
            Log.d(ContentValues.TAG, "password -->"+ password)
        }
        //cancel button click of custom layout
        dialogCancelBtn.setOnClickListener {
            //dismiss dialog
           //dismiss()
        }
    }
}
