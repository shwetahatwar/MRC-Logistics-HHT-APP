package com.briot.mrclogistics.implementor

import android.content.ContentValues
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.briot.mrclogistics.implementor.repository.local.PrefConstants
import com.briot.mrclogistics.implementor.repository.local.PrefRepository
import com.briot.mrclogistics.implementor.repository.remote.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhysicalStockVerificationViewModel : ViewModel() {
        var materialBarcode: String? = ""
        var userId: Int=0
        var logedInUsername: String? = ""

        val TAG = "PhysicalStockVerificationViewModel"

        val networkError: LiveData<Boolean> = MutableLiveData()
        val auditItems: LiveData<Array<AuditItem?>> = MutableLiveData()
        val invalidAuditItems: Array<AuditItem?> = arrayOf(null)
        val invalidAuditPutItems: LiveData<Array<AuditItemResponse?>> = MutableLiveData()
        val users: LiveData<Array<User?>> = MutableLiveData()
        var userResponseData: Array<User?> = arrayOf(null)

        fun handleSubmitAudit() {
            var auditItems = AuditItem()
            auditItems.materialBarcode = materialBarcode
            //auditItems.userId=userId
            // auditItems.userId = PrefRepository.singleInstance.getValueOrDefault(PrefConstants().USER_ID, "0").toInt()
            for (item in userResponseData) {
                if (item!!.username == logedInUsername){
                    Log.d(ContentValues.TAG, "item ----id " + item!!.id)
                    auditItems.userId = item.id
                }
            }

            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    (networkError as MutableLiveData<Boolean>).value = false
                }
            }

            RemoteRepository.singleInstance.postAuditsItems(auditItems,this::handleAuditItemsResponse, this::handleAuditItemsError)
        }
//            RemoteRepository.singleInstance.postAuditsItems(AuditItem,this::handleAuditItemsResponse, this::handleAuditItemsError)
//        }

    fun getUsers(){
        RemoteRepository.singleInstance.getUsers(
                this::handleUserResponse, this::handleAuditItemsError)
    }

    private fun handleUserResponse(users: Array<User?>) {

        userResponseData = users
        Log.d(ContentValues.TAG, "item  response----- " + userResponseData)
        Log.d(ContentValues.TAG, "item  handleUserResponse----- " + userResponseData[1]!!.username)


        (this.users as MutableLiveData<Array<User?>>).value = users
        // return handleUserResponse(users)
//        return this.users
        // Log.d(TAG, "Handle get response......." + putawayItems)
        // responsePutawayLoadingItems = putawayItems
    }


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