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

    val TAG = "VendorMaterialScanViewModel"

    val networkError: LiveData<Boolean> = MutableLiveData()
    val vendorItems: LiveData<Array<VendorMaterialInward?>> = MutableLiveData()
    val invalidVendorItems: Array<VendorMaterialInward?> = arrayOf(null)
    val invalidvendorPutItems: LiveData<Array<PostVendorResponse?>> = MutableLiveData()

    fun handleSubmitVendor() {
        var VendorMaterialInward = VendorMaterialInward()
        VendorMaterialInward.materialBarcode = materialBarcode
        VendorMaterialInward.userId = userId


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
}