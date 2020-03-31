package com.briot.balmerlawrie.implementor.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.briot.balmerlawrie.implementor.UiHelper
import com.briot.balmerlawrie.implementor.repository.remote.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VendorMaterialScanViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var materialBarcode: String? = ""
    var userId: String? = ""
    var logedInUsername: String? = ""

    val TAG = "VendorMaterialScanViewModel"

    val networkError: LiveData<Boolean> = MutableLiveData()
    val vendorItems: LiveData<Array<VendorMaterialInward?>> = MutableLiveData()
    val invalidVendorItems: Array<VendorMaterialInward?> = arrayOf(null)
    val invalidvendorPutItems: LiveData<Array<PostVendorResponse?>> = MutableLiveData()
    val users: LiveData<Array<User?>> = MutableLiveData()
    var userResponseData: Array<User?> = arrayOf(null)
    // var username: String?=""

//    fun getUserID(responseData: Array<>, logedInUsername: String?){
//        Log.d(ContentValues.TAG, "from function userResponseData ---- " + responseData)
//        Log.d(ContentValues.TAG, "from function logedInUsername ---- " + logedInUsername)
//    }

    fun handleSubmitVendor() {
        var VendorMaterialInward = VendorMaterialInward()

        VendorMaterialInward.materialBarcode = materialBarcode
        // VendorMaterialInward.userId = userId

//        Log.d(ContentValues.TAG, "item logedInUsername ---- " + logedInUsername)
//        Log.d(ContentValues.TAG, "item userResponseData ---- " + userResponseData[1]!!.id)
//        Log.d(ContentValues.TAG, "item userResponseData ---- " + userResponseData[1]!!.username)

        for (item in userResponseData) {
            if (item!!.username == logedInUsername){
                Log.d(ContentValues.TAG, "item ----id " + item!!.id)
                VendorMaterialInward.userId = item.id?.toString()
            }
        }
        Log.d(ContentValues.TAG, "item ---- VendorMaterialInward " + VendorMaterialInward)

        // username = logedInUsername
//        Log.d(ContentValues.TAG, "userResponseData ---- " + userResponseData[1]!!.username)
//        Log.d(ContentValues.TAG, "logedInUsername ---- " + logedInUsername)

        // this.getUserID(userResponseData, logedInUsername)

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                (networkError as MutableLiveData<Boolean>).value = false
            }
        }
        RemoteRepository.singleInstance.postMaterialInwards(VendorMaterialInward,
                this::handleVendorItemsResponse, this::handleVendorItemsError)
    }

    private fun handleVendorItemsResponse(postVendorResponse: PostVendorResponse?) {
       // (this.vendorItems as MutableLiveData<Array<VendorMaterialInward?>>).value = vendorItems
      // Log.d(TAG,"Data Putaway Put Response"+ postVendorResponse)
        Log.d(ContentValues.TAG, " response ----- " + postVendorResponse)
    }

    private fun handleVendorItemsError(error: Throwable) {
        if (UiHelper.isNetworkError(error)) {
            (networkError as MutableLiveData<Boolean>).value = true
        } else {
            (this.vendorItems as MutableLiveData<Array<VendorMaterialInward?>>).value = invalidVendorItems
        }
    }

    fun getUsers(){
        RemoteRepository.singleInstance.getUsers(
                this::handleUserResponse, this::handleVendorItemsError)
    }

    private fun handleUserResponse(users: Array<User?>) {

        userResponseData = users
        Log.d(ContentValues.TAG, "item  response----- " + userResponseData)
        Log.d(ContentValues.TAG, "item  handleUserResponse----- " + userResponseData[1]!!.username)


        (this.users as MutableLiveData<Array<User?>>).value = users
        // return handleUserResponse(users)
//        return this.users
        // Log.d(TAG, "Handle get response......." + putawayItems)
        // responsePutawayLoadingItems = putawayItems
    }

}