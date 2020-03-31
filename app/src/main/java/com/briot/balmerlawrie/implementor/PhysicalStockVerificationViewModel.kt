package com.briot.balmerlawrie.implementor

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.briot.balmerlawrie.implementor.repository.remote.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhysicalStockVerificationViewModel : ViewModel() {
        var materialBarcode: String? = ""
        var userId: String? = ""

        val TAG = "PhysicalStockVerificationViewModel"

        val networkError: LiveData<Boolean> = MutableLiveData()
        val vendorItems: LiveData<Array<AuditItem?>> = MutableLiveData()
        val invalidVendorItems: Array<AuditItem?> = arrayOf(null)
        val invalidvendorPutItems: LiveData<Array<AuditItemResponse?>> = MutableLiveData()

        fun handleSubmitAudit() {
            var auditItems = AuditItem()
            auditItems.materialBarcode = materialBarcode
            auditItems.userId = userId


            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    (networkError as MutableLiveData<Boolean>).value = false
                }
            }

            RemoteRepository.singleInstance.postAuditsItems(auditItems,this::handleAuditItemsResponse, this::handleAuditItemsError)
        }

//
//            RemoteRepository.singleInstance.postAuditsItems(AuditItem,this::handleAuditItemsResponse, this::handleAuditItemsError)
//        }

        private fun handleAuditItemsResponse(auditItemResponse: AuditItemResponse?) {
            // (this.vendorItems as MutableLiveData<Array<VendorMaterialInward?>>).value = vendorItems
            // Log.d(TAG,"Data Putaway Put Response"+ postVendorResponse)
            Log.d(ContentValues.TAG, " response ----- " + auditItemResponse)
        }

        private fun handleAuditItemsError(error: Throwable) {
            if (UiHelper.isNetworkError(error)) {
                (networkError as MutableLiveData<Boolean>).value = true
            } else {
                (this.vendorItems as MutableLiveData<Array<AuditItem?>>).value = invalidVendorItems
            }
        }
    }