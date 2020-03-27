package com.briot.balmerlawrie.implementor.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.briot.balmerlawrie.implementor.R
import com.briot.balmerlawrie.implementor.UiHelper
import com.briot.balmerlawrie.implementor.repository.remote.PickingItems
import com.briot.balmerlawrie.implementor.repository.remote.RemoteRepository

class PickingViewModel : ViewModel() {
    var rackBarcodeSerial: String? = ""
    var binBarcodeSerial: String? = ""
    var materialBarcodeSerial: String? = ""

    val TAG = "PickingViewModel"

    val networkError: LiveData<Boolean> = MutableLiveData()
    val pickingItems: LiveData<Array<PickingItems?>> = MutableLiveData()
    val invalidPickingItems: Array<PickingItems?> = arrayOf(null)

    fun loadPickingItems(status: String) {
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

}
