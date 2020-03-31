package com.briot.balmerlawrie.implementor

import android.content.ContentValues
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.briot.balmerlawrie.implementor.repository.local.PrefConstants
import com.briot.balmerlawrie.implementor.repository.local.PrefRepository
import com.briot.balmerlawrie.implementor.repository.remote.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhysicalStockVerificationViewModel : ViewModel() {
        var materialBarcode: String? = ""
        var userId: Int=0

        val TAG = "PhysicalStockVerificationViewModel"

        val networkError: LiveData<Boolean> = MutableLiveData()
        val auditItems: LiveData<Array<AuditItem?>> = MutableLiveData()
        val invalidAuditItems: Array<AuditItem?> = arrayOf(null)
        val invalidAuditPutItems: LiveData<Array<AuditItemResponse?>> = MutableLiveData()

        fun handleSubmitAudit() {
            var auditItems = AuditItem()
            auditItems.materialBarcode = materialBarcode
            //auditItems.userId=userId
            auditItems.userId = PrefRepository.singleInstance.getValueOrDefault(PrefConstants().USER_ID, "0").toInt()


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
                (this.auditItems as MutableLiveData<Array<AuditItem?>>).value = invalidAuditItems
            }
        }
    }