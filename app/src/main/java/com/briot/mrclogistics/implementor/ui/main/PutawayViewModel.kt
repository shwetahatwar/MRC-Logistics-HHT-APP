package com.briot.mrclogistics.implementor.ui.main

import android.util.Log
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.briot.mrclogistics.implementor.MainApplication
import com.briot.mrclogistics.implementor.R
import com.briot.mrclogistics.implementor.UiHelper
import com.briot.mrclogistics.implementor.data.AppDatabase
import com.briot.mrclogistics.implementor.repository.local.PrefConstants
import com.briot.mrclogistics.implementor.repository.remote.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.briot.mrclogistics.implementor.repository.remote.PutawayItems

class PutawayViewModel : ViewModel() {

    // var id: Int = 0
    var rackBarcodeSerial: String? = ""
    var binBarcodeSerial: String? = ""
    var materialBarcodeSerial: String? = ""

    val TAG = "PutawayViewModel"

    val networkError: LiveData<Boolean> = MutableLiveData()
    val putawayItems: LiveData<Array<PutawayItems?>> = MutableLiveData()
    val invalidPutawayItems: Array<PutawayItems?> = arrayOf(null)
    //val invalidPutawayPutItems: LiveData<Array<PutPutawayResponse?>> = MutableLiveData()
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

        // Log.d(TAG, "Handle get response......." + putawayItems)
        // responsePutawayLoadingItems = putawayItems
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

        val binB = "BIN004"
        val rackB = "RACK004"
        val matB = "NSN2017-468-160,SN0205,BP,M20x1.5x13x30 Gr8HT PLSLT ,300302790,300,4210-00006,39.885,11.97,12.42,101166,05.01.2020,12.03.2020,120320121418249"
         // Log.d(TAG, "getResponsePutwayData,......." + getResponsePutwayData[1]!!.binBarcodeSerial)
//        for (item in getResponsePutwayData) {
//            if (binB == item!!.binBarcodeSerial && rackB == item!!.rackBarcodeSerial && matB == item!!.materialBarcodeSerial){
//         Log.d(TAG, "yes-------------"+binB)
//         Log.d(TAG, "yes-------------"+item!!.binBarcodeSerial)
//            }
//            else{
//                Log.d(TAG, "noooooooo.")
//            }
//    }
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                (networkError as MutableLiveData<Boolean>).value = false
            }
        }

// code with id
//        RemoteRepository.singleInstance.putPutawayItems(id, putawayRequestObject,
//                this::handlePutawayPutItemsResponse, this::handlePutawayPutItemsError)

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
