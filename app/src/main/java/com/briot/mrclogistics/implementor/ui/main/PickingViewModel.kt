package com.briot.mrclogistics.implementor.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.briot.mrclogistics.implementor.UiHelper
import com.briot.mrclogistics.implementor.repository.remote.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PickingViewModel : ViewModel() {

    var id: Int = 0
    var rackBarcodeSerial: String? = ""
    var binBarcodeSerial: String? = ""
    var materialBarcodeSerial: String? = ""

    val TAG = "PickingViewModel"

    val networkError: LiveData<Boolean> = MutableLiveData()
    val pickingItems: LiveData<Array<PickingItems?>> = MutableLiveData()
    val invalidPickingItems: Array<PickingItems?> = arrayOf(null)

    var responsePickingLoadingItems: Array<PickingItems?> = arrayOf(null)

    var errorMessage: String = ""

    fun loadPickingItems() {
        (networkError as MutableLiveData<Boolean>).value = false
        (this.pickingItems as MutableLiveData<Array<PickingItems?>>).value = emptyArray()

        RemoteRepository.singleInstance.getPickingItems(this::handlePickingItemsResponse, this::handlePickingItemsError)
    }

    private fun handlePickingItemsResponse(pickingItems: Array<PickingItems?>) {
        (this.pickingItems as MutableLiveData<Array<PickingItems?>>).value = pickingItems

    }

    private fun handlePickingItemsError(error: Throwable) {

        if (UiHelper.isNetworkError(error)) {
            (networkError as MutableLiveData<Boolean>).value = true
        } else {
            (this.pickingItems as MutableLiveData<Array<PickingItems?>>).value = invalidPickingItems
        }
    }

    suspend fun handleSubmitPicking() {
        var pickingRequestObject = PickingItems()
        pickingRequestObject.binBarcodeSerial = binBarcodeSerial
        pickingRequestObject.materialBarcodeSerial = materialBarcodeSerial
        pickingRequestObject.rackBarcodeSerial = rackBarcodeSerial

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                (networkError as MutableLiveData<Boolean>).value = false
            }
        }

        RemoteRepository.singleInstance.putPickingItems(pickingRequestObject,
                this::handlePickingPutItemsResponse, this::handlePickingPutItemsError)
    }
    private fun handlePickingPutItemsResponse(putPickingResponse: PutPickingResponse?) {
        Log.d(TAG, "Data updated successfully")
    }

    private fun handlePickingPutItemsError(error: Throwable) {
        Log.d(TAG, error.localizedMessage)
    }
}



