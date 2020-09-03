package com.briot.mrclogistics.implementor.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import android.util.Log
//import com.briot.mrclogistics.implementor.repository.remote.PopulatedUser
import com.briot.mrclogistics.implementor.repository.remote.RemoteRepository
import com.briot.mrclogistics.implementor.repository.remote.SignInResponse
import com.briot.mrclogistics.implementor.repository.remote.User
import com.google.gson.JsonParser
import io.reactivex.exceptions.CompositeException
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import org.json.JSONObject

class LoginViewModel : ViewModel() {

    val TAG = "LoginViewModel"

    val signInResponse: LiveData<SignInResponse> = MutableLiveData<SignInResponse>()
    var errorMessage: String = ""
    val networkError: LiveData<Boolean> = MutableLiveData<Boolean>()
//    val invalidUser: PopulatedUser = PopulatedUser()

    fun loginUser(username: String, password: String , deviceId: String) {
        (networkError as MutableLiveData<Boolean>).value = false
        RemoteRepository.singleInstance.loginUser(username, password, deviceId ,this::handleLoginResponse, this::handleLoginError)
    }

    private fun handleLoginResponse(signInResponse: SignInResponse) {
        Log.d(TAG, "successful user" + signInResponse.toString())
        (this.signInResponse as MutableLiveData<SignInResponse>).value = signInResponse
    }

    private fun handleLoginError(error: Throwable) {
        Log.d(TAG, "error------> "+ error.localizedMessage)
        if (error is HttpException) {
            if (error.code() >= 401) {
                var msg = error.response()?.errorBody()?.string()
                try{
                    var message = JsonParser().parse(msg)
                            .asJsonObject["message"]
                            .asString
                    if (message != null && message.isNotEmpty()) {
                        errorMessage = message + " Please enter valid Credentials."
                    } else {
                        errorMessage = error.message()
                    }
                } catch (e: Exception){
                    errorMessage = "Please enter valid Credentials."
                }

            }
            (networkError as MutableLiveData<Boolean>).value = true
        } else if (error is SocketException || error is SocketTimeoutException || error is CompositeException) {
            (networkError as MutableLiveData<Boolean>).value = true
        } else {
//            (this.user as MutableLiveData<PopulatedUser>).value = invalidUser
        }
    }
}
