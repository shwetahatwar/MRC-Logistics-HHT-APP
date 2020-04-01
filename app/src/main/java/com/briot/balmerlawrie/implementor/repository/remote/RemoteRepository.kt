package com.briot.balmerlawrie.implementor.repository.remote

import android.content.ContentValues.TAG
import android.util.Log
import android.nfc.tech.NfcBarcode
import com.briot.balmerlawrie.implementor.RetrofitHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.reflect.KFunction1

class RemoteRepository {
    companion object {
        val singleInstance = RemoteRepository();
    }

    fun loginUser(username: String, password: String, deviceId: String, handleResponse: (SignInResponse) -> Unit, handleError: (Throwable) -> Unit) {
        var signInRequest: SignInRequest = SignInRequest();
        signInRequest.username = username;
        signInRequest.password = password;
        signInRequest.deviceId = deviceId;
        RetrofitHelper.retrofit.create(ApiInterface::class.java)
                .login(signInRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResponse, handleError)
    }

    fun getUsers(handleResponse: (Array<User?>) -> Unit, handleError: (Throwable) -> Unit) {
        RetrofitHelper.retrofit.create(ApiInterface::class.java)
                .getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResponse, handleError)
    }

    fun getMaterialDetails(barcodeSerial: String, handleResponse: (Array<MaterialInward>) -> Unit, handleError: (Throwable) -> Unit) {
        RetrofitHelper.retrofit.create(ApiInterface::class.java)
                .getMaterialDetails(barcodeSerial)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResponse, handleError)
    }

    fun getDispatchSlip(dispatchSlipId: String, handleResponse: (Array<DispatchSlip>) -> Unit, handleError: (Throwable) -> Unit) {
        RetrofitHelper.retrofit.create(ApiInterface::class.java)
                .getDispatchSlip(dispatchSlipId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResponse, handleError)
    }

    fun getAssignedPickerDispatchSlips(userId: Int, handleResponse: (Array<DispatchSlip?>) -> Unit, handleError: (Throwable) -> Unit) {
        RetrofitHelper.retrofit.create(ApiInterface::class.java)
                .getAssignedPickerDispatchSlips(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResponse, handleError)
    }

    fun getAssignedLoaderDispatchSlips(userId: Int, handleResponse: (Array<DispatchSlip?>) -> Unit, handleError: (Throwable) -> Unit) {
        RetrofitHelper.retrofit.create(ApiInterface::class.java)
                .getAssignedLoaderDispatchSlips(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResponse, handleError)
    }

    fun getDispatchSlipItems(dispatchSlipId: Int, handleResponse: (Array<DispatchSlipItem?>) -> Unit, handleError: (Throwable) -> Unit) {
        RetrofitHelper.retrofit.create(ApiInterface::class.java)
                .getDispatchSlipMaterials(dispatchSlipId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResponse, handleError)
    }

    fun postDispatchSlipPickedMaterials(dispatchSlipId: Int, requestbody: DispatchSlipRequest, handleResponse: (DispatchSlipItemResponse?) -> Unit, handleError: (Throwable) -> Unit) {
        RetrofitHelper.retrofit.create(ApiInterface::class.java)
                .postDispatchSlipPickedMaterials(dispatchSlipId, requestbody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResponse, handleError)
    }

    fun postDispatchSlipLoadedMaterials(dispatchSlipId: Int, requestbody: DispatchSlipRequest,
                                        handleResponse: (DispatchSlipItemResponse?) -> Unit,
                                        handleError: (Throwable) -> Unit) {
        RetrofitHelper.retrofit.create(ApiInterface::class.java)
                .postDispatchSlipLoadedMaterials(dispatchSlipId, requestbody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResponse, handleError)
    }

    fun getProjects(status: String, handleResponse: (Array<Project?>) -> Unit, handleError: (Throwable) -> Unit) {
        RetrofitHelper.retrofit.create(ApiInterface::class.java)
                .getAuditProjects(status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResponse, handleError)
    }


    fun getPutaway( handleResponse: KFunction1<Array<PutawayItems?>, Unit>, handleError: (Throwable) -> Unit) {
        RetrofitHelper.retrofit.create(ApiInterface::class.java)
                .getPutaway()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResponse, handleError)

        Log.d(TAG,"handle response in remote" + handleResponse)

    }

//    fun putPutawayItems(id: Int, requestbody: PutawayItems, handleResponse: (PutPutawayResponse?) -> Unit, handleError: (Throwable) -> Unit) {
//        RetrofitHelper.retrofit.create(ApiInterface::class.java)
//                .putPutawayItems(id, requestbody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(handleResponse, handleError)
//    }

    fun putPutawayItems(requestbody: PutawayItems, handleResponse: (PutPutawayResponse?) -> Unit, handleError: (Throwable) -> Unit) {
        RetrofitHelper.retrofit.create(ApiInterface::class.java)
                .putPutawayItems(requestbody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResponse, handleError)
    }

    fun getPickingItems(handleResponse: KFunction1<Array<PickingItems?>, Unit>, handleError: (Throwable) -> Unit) {
        RetrofitHelper.retrofit.create(ApiInterface::class.java)
                .getPickingItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResponse, handleError)  
    }

    fun postMaterialInwards(requestbody: VendorMaterialInward,
                            handleResponse: (VendorMaterialInward?) -> Unit,
                            handleError: (Throwable) -> Unit) {
            RetrofitHelper.retrofit.create(ApiInterface::class.java)
                .postMaterialInwards(requestbody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResponse, handleError)
    }
//    fun putPickingItems(id: Int, requestbody: PickingRequest, handleResponse: (PutPickingResponse?) -> Unit, handleError: (Throwable) -> Unit) {
//        RetrofitHelper.retrofit.create(ApiInterface::class.java)
//                .putPickingItems(id, requestbody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(handleResponse, handleError)
//    }

        fun putPickingItems(requestbody: PickingItems, handleResponse: (PutPickingResponse?) -> Unit, handleError: (Throwable) -> Unit) {
            RetrofitHelper.retrofit.create(ApiInterface::class.java)
                    .putPickingItems(requestbody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(handleResponse, handleError)
        }

        //    fun postAuditsItems( requestbody: AuditItem, handleResponse: (AuditItemResponse?) -> Unit, handleError: (Throwable) -> Unit) {
//        RetrofitHelper.retrofit.create(ApiInterface::class.java)
//                .postAuditsItems(requestbody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(handleResponse, handleError)
//    }
        fun postAuditsItems(requestbody: AuditItem, handleResponse: (AuditItemResponse?) -> Unit, handleError: (Throwable) -> Unit) {
            RetrofitHelper.retrofit.create(ApiInterface::class.java)
                    .postAuditsItems(requestbody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(handleResponse, handleError)
        }
}