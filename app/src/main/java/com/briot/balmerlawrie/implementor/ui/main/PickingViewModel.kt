package com.briot.balmerlawrie.implementor.ui.main

import android.util.Log
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.briot.balmerlawrie.implementor.R
import com.briot.balmerlawrie.implementor.UiHelper
import com.briot.balmerlawrie.implementor.repository.local.PrefConstants
import com.briot.balmerlawrie.implementor.repository.remote.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PickingViewModel : ViewModel() {

    // var id: Int = 0
    var rackBarcodeSerial: String? = ""
    var binBarcodeSerial: String? = ""
    var materialBarcodeSerial: String? = ""

    val TAG = "PickingViewModel"


    val invalidPickingPutItems: LiveData<Array<PutPickingResponse?>> = MutableLiveData()
    //val invalidputawayloadingItems: Array<DispatchSlipItem?> = arrayOf(null)
    var getResponsePickingData: Array<PickingItems?> = arrayOf(null)




    val networkError: LiveData<Boolean> = MutableLiveData()
    val itemSubmissionSuccessful: LiveData<Boolean> = MutableLiveData()
    val pickingItems: LiveData<Array<PickingItems?>> = MutableLiveData()
    val invalidPickingItems: Array<PickingItems?> = arrayOf(null)

    var responsePickingLoadingItems: Array<PickingItems?> = arrayOf(null)
    // val invalidDispatchloadingItems: Array<DispatchSlipItem?> = arrayOf(null)
    // val invalidpickingloadingItems: Array<PickingItems?> = arrayOf(null)
    var errorMessage: String = ""

    fun loadPickingItems(status: String) {
        (networkError as MutableLiveData<Boolean>).value = false
        (this.pickingItems as MutableLiveData<Array<PickingItems?>>).value = emptyArray()

        RemoteRepository.singleInstance.getPickingItems(this::handlePickingItemsResponse, this::handlePickingItemsError)
    }

    private fun handlePickingItemsResponse(pickingItems: Array<PickingItems?>) {
        (this.pickingItems as MutableLiveData<Array<PickingItems?>>).value = pickingItems

        getResponsePickingData = pickingItems
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

        val binB = "BIN004"
        val rackB = "RACK004"
        val matB = "NSN2017-468-160,SN0205,BP,M20x1.5x13x30 Gr8HT PLSLT ,300302790,300,4210-00006,39.885,11.97,12.42,101166,05.01.2020,12.03.2020,120320121418249"


//        Log.d(TAG, "materialBarcodeSerial -----"+ putawayRequestObject.materialBarcodeSerial)
//        Log.d(TAG, "rackBarcodeSerial -----"+ putawayRequestObject.rackBarcodeSerial)

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                (networkError as MutableLiveData<Boolean>).value = false
            }
        }

        RemoteRepository.singleInstance.putPickingItems(pickingRequestObject,
                this::handlePickingPutItemsResponse, this::handlePickingPutItemsError)
    }
    private fun handlePickingPutItemsResponse(putPickingResponse: PutPickingResponse?) {
        (itemSubmissionSuccessful as MutableLiveData<Boolean>).value = true

    }

    //Log.d(TAG, "Data updated successfully")

    private fun handlePickingPutItemsError(error: Throwable) {
        Log.d(TAG, error.localizedMessage)


//        if (UiHelper.isNetworkError(error)) {
//            (networkError as MutableLiveData<Boolean>).value = true
//        } else {
//            (this.putawayItems as MutableLiveData<Array<PutPutawayResponse?>>).value = invalidPutawayPutItems
//        }
    }

}



