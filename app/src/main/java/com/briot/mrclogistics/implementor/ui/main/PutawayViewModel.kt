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
import com.briot.mrclogistics.implementor.repository.remote.PutawayItems

class PutawayViewModel : ViewModel() {

    var rackBarcodeSerial: String? = ""
    var binBarcodeSerial: String? = ""
    var materialBarcodeSerial: String? = ""

    val TAG = "PutawayViewModel"

    val networkError: LiveData<Boolean> = MutableLiveData()
    val putawayItems: LiveData<Array<PutawayItems?>> = MutableLiveData()
    val invalidPutawayItems: Array<PutawayItems?> = arrayOf(null)
    var responsePutawayLoadingItems: Array<PutawayItems?> = arrayOf(null)
    val invalidputawayloadingItems: Array<DispatchSlipItem?> = arrayOf(null)
    var getResponsePutwayData: Array<PutawayItems?> = arrayOf(null)
    var errorMessage: String = ""


    fun loadPutawayItems() {
        (networkError as MutableLiveData<Boolean>).value = false
        (this.putawayItems as MutableLiveData<Array<PutawayItems?>>).value = emptyArray()

        RemoteRepository.singleInstance.getPutaway(this::handlePutawayItemsResponse, this::handlePutawayItemsError)
    }

    private fun handlePutawayItemsResponse(putawayItems: Array<PutawayItems?>) {

        getResponsePutwayData = putawayItems
        (this.putawayItems as MutableLiveData<Array<PutawayItems?>>).value = putawayItems
    }

    private fun handlePutawayItemsError(error: Throwable) {
        if (UiHelper.isNetworkError(error)) {
            (networkError as MutableLiveData<Boolean>).value = true
        } else {
            (this.putawayItems as MutableLiveData<Array<PutawayItems?>>).value = invalidPutawayItems
        }
    }

    fun handleSubmitPutaway() {
        var putawayRequestObject = PutawayItems()
        putawayRequestObject.binBarcodeSerial = binBarcodeSerial
        putawayRequestObject.materialBarcodeSerial = materialBarcodeSerial
        putawayRequestObject.rackBarcodeSerial = rackBarcodeSerial

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                (networkError as MutableLiveData<Boolean>).value = false
            }
        }

        // put call for putaway
        RemoteRepository.singleInstance.putPutawayItems(putawayRequestObject,
                this::handlePutawayPutItemsResponse, this::handlePutawayPutItemsError)
    }

    private fun handlePutawayPutItemsResponse(putPutawayResponse: PutPutawayResponse?) {
        Log.d(TAG, "Data Putaway Put Response"+ putPutawayResponse)
        //latest putaway response
    }

    private fun handlePutawayPutItemsError(error: Throwable) {
        Log.d(TAG, error.localizedMessage)
    }
}
