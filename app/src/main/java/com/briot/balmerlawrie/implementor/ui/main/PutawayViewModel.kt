package com.briot.balmerlawrie.implementor.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.briot.balmerlawrie.implementor.MainApplication
import com.briot.balmerlawrie.implementor.UiHelper
import com.briot.balmerlawrie.implementor.data.AppDatabase
import com.briot.balmerlawrie.implementor.repository.remote.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.Arrays.toString

class PutawayViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var id: Int = 0
    var rackBarcodeSerial: String? = ""
    var binBarcodeSerial: String? = ""
    var materialBarcodeSerial: String? = ""

    val TAG = "PutawayViewModel"

    val networkError: LiveData<Boolean> = MutableLiveData()
    val putawayItems: LiveData<Array<PutawayItems?>> = MutableLiveData()
    val invalidPutawayItems: Array<PutawayItems?> = arrayOf(null)
    val invalidPutawayPutItems: LiveData<Array<PutPutawayResponse?>> = MutableLiveData()
    var responsePutawayLoadingItems: Array<PutawayItems?> = arrayOf(null)
    // val invalidDispatchloadingItems: Array<DispatchSlipItem?> = arrayOf(null)
    val invalidputawayloadingItems: Array<DispatchSlipItem?> = arrayOf(null)
    var errorMessage: String = ""


    fun loadPutawayItems(status: String) {
        (networkError as MutableLiveData<Boolean>).value = false
        (this.putawayItems as MutableLiveData<Array<PutawayItems?>>).value = emptyArray()

        RemoteRepository.singleInstance.getPutaway( this::handlePutawayItemsResponse, this::handlePutawayItemsError)
        // Log.d(TAG, "abcdefthis::handlePutawayItemsResponse,......." + this::handlePutawayItemsResponse.toString())

    }

    private fun handlePutawayItemsResponse(putawayItems: Array<PutawayItems?>) {

        (this.putawayItems as MutableLiveData<Array<PutawayItems?>>).value = putawayItems
        //Log.d(TAG, "abcdef......." + this.putawayItems.value)
        // responsePutawayLoadingItems = putawayItems

    }

    private fun handlePutawayItemsError(error: Throwable) {
     //   Log.d(TAG, error.localizedMessage)

        if (UiHelper.isNetworkError(error)) {
            (networkError as MutableLiveData<Boolean>).value = true
        } else {
            (this.putawayItems as MutableLiveData<Array<PutawayItems?>>).value = invalidPutawayItems
        }
    }


    suspend fun handleSubmitPutaway() {
        var putawayRequestObject = PutawayItems()
        putawayRequestObject.binBarcodeSerial = binBarcodeSerial
        putawayRequestObject.materialBarcodeSerial = materialBarcodeSerial
        putawayRequestObject.rackBarcodeSerial = rackBarcodeSerial

//        Log.d(TAG, "materialBarcodeSerial -----"+ putawayRequestObject.materialBarcodeSerial)
//        Log.d(TAG, "rackBarcodeSerial -----"+ putawayRequestObject.rackBarcodeSerial)

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                (networkError as MutableLiveData<Boolean>).value = false
            }
        }

// code with id
//        RemoteRepository.singleInstance.putPutawayItems(id, putawayRequestObject,
//                this::handlePutawayPutItemsResponse, this::handlePutawayPutItemsError)

        RemoteRepository.singleInstance.putPutawayItems(putawayRequestObject,
                this::handlePutawayPutItemsResponse, this::handlePutawayPutItemsError)
    }

    private fun handlePutawayPutItemsResponse(putPutawayResponse: PutPutawayResponse?) {
        Log.d(TAG, "Data updated successfully")
    }

    private fun handlePutawayPutItemsError(error: Throwable) {
        Log.d(TAG, error.localizedMessage)

//        if (UiHelper.isNetworkError(error)) {
//            (networkError as MutableLiveData<Boolean>).value = true
//        } else {
//            (this.putawayItems as MutableLiveData<Array<PutPutawayResponse?>>).value = invalidPutawayPutItems
//        }
    }

}
