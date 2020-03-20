package com.briot.balmerlawrie.implementor.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.briot.balmerlawrie.implementor.UiHelper
import com.briot.balmerlawrie.implementor.repository.remote.DispatchSlipItem
import com.briot.balmerlawrie.implementor.repository.remote.PutawayItems
import com.briot.balmerlawrie.implementor.repository.remote.RemoteRepository

class PutawayViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var rackBarcodeSerial: String? = ""
    var binBarcodeSerial: String? = ""
    var materialBarcodeSerial: String? = ""

    val TAG = "PutawayViewModel"

    val networkError: LiveData<Boolean> = MutableLiveData()
    val putawayItems: LiveData<Array<PutawayItems?>> = MutableLiveData()
    val invalidPutawayItems: Array<PutawayItems?> = arrayOf(null)

    fun loadPutawayItems(status: String) {
        (networkError as MutableLiveData<Boolean>).value = false
        (this.putawayItems as MutableLiveData<Array<PutawayItems?>>).value = emptyArray()

        RemoteRepository.singleInstance.getPutaway(status, this::handlePutawayItemsResponse, this::handlePutawayItemsError)
    }

    private fun handlePutawayItemsResponse(putawayItems: Array<PutawayItems?>) {
        Log.d(TAG, "successful dispatch picking items details" + putawayItems.toString())
       // (this.putawayItems as MutableLiveData<Array<PutawayItems?>>).value = putawayItems

    }

    private fun handlePutawayItemsError(error: Throwable) {
     //   Log.d(TAG, error.localizedMessage)

        if (UiHelper.isNetworkError(error)) {
            (networkError as MutableLiveData<Boolean>).value = true
        } else {
            (this.putawayItems as MutableLiveData<Array<PutawayItems?>>).value = invalidPutawayItems
        }
    }

}
