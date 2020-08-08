package com.briot.mrclogistics.implementor
import com.briot.mrclogistics.implementor.repository.local.PrefConstants
import com.briot.mrclogistics.implementor.repository.local.PrefRepository
import com.briot.mrclogistics.implementor.repository.remote.RemoteRepository
import com.briot.mrclogistics.implementor.repository.remote.SignInResponse
class LoginClass {
    var sendResponse = false;
    var checkElement = 0;
    companion object {
        val newLogin = LoginClass();
    }
    fun checkLogin(): Boolean {
//        checkElement = 0;
//        val username = PrefRepository.singleInstance.getValueOrDefault(PrefConstants().username, "")
//        val password = PrefRepository.singleInstance.getValueOrDefault(PrefConstants().password, "")
//        val deviceId = PrefRepository.singleInstance.getValueOrDefault(PrefConstants().deviceId, "")
//        RemoteRepository.singleInstance.loginUser(username, password, deviceId ,this::handleLoginResponse, this::handleLoginError)
//        while(checkElement == 0){
//
//        }
//        return sendResponse
        var savedId: String = PrefRepository.singleInstance.getValueOrDefault(PrefConstants().id, "")
        return savedId != ""
    }
//    private fun handleLoginResponse(signInResponse: SignInResponse) {
//        checkElement = 1;
//        sendResponse = true
//    }
//
//    private fun handleLoginError(error: Throwable) {
//        checkElement = 1;
//        sendResponse = false
//    }
}