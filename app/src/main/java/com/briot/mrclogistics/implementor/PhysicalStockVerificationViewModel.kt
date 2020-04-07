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

    fun getUsers(){
        RemoteRepository.singleInstance.getUsers(
                this::handleUserResponse, this::handleAuditItemsError)
    }

    private fun handleUserResponse(users: Array<User?>) {

        userResponseData = users
        (this.users as MutableLiveData<Array<User?>>).value = users
    }
        private fun handleAuditItemsResponse(auditItemResponse: AuditItemResponse?) {
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