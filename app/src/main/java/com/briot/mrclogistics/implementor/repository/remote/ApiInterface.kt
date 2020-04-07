package com.briot.mrclogistics.implementor.repository.remote

import io.reactivex.Observable
import retrofit2.http.*


class SignInRequest {
    var username: String? = null
    var password: String? = null
    var deviceId: String? = null
}

class SignInResponse {
    var userId: Number? = null
    var token: String? =  null
    var id: String? = null
    var username: String? = null
    var password: String? = null
    var deviceId: String? = null
    var status: Number? = null
}

class Role {
    var id: Number? = null
    var roleName:  String? = null
}

class User {
    var username: String? = null
    var id: Number? = null
    var token: String? = null
}

class Material {
    var materialType: String? = null
    var materialCode: String? = null
    var materialDescription: String? = null
    var genericName: String? = null
    var packingType: String? = null
    var packSize: String? = null
    var netWeight: String? = null
    var grossWeight: String? = null
    var tareWeight: String? = null
    var UOM: String? = null
    var batchCode: String?  = null
    var status: String? = null
//    var createdBy: User? = null
//    var updatedBy: User? = null
}
class PutawayItems {
    var rackBarcodeSerial: String? = null
    var binBarcodeSerial: String? = null
    var materialBarcodeSerial: String? = null
}

class DispatchSlip {
    var id: Number? = null
    var dispatchSlipNumber: String = ""
    var truckId: Number? = null
    var depoId: Number? = null
    var status: String = ""
    var ttat: Ttat? = null
    var dispatchSlipStatus: String? = null
    var depot: Depo? = null
    var createdBy: String? = null
    var updatedBy: String? = null
    var createdAt: String? = null
    var updatdAt: String? = null
}

class  MaterialInward {
    var materialId: Number? = null
    var materialCode: Number = 0
    var batchNumber: String? = null
    var serialNumber: String? = null
    var isScrapped: Boolean = false
    var isInward: Boolean = false
    var dispatchSlipId: Number? = null
    var status: Boolean = false
    var dispatchSlip: DispatchSlip? = null
    var material: Material? = null
//    var createdBy: User? = null
//    var updatedBy: User? = null
    }

class VendorMaterialInward{
    var materialBarcode: String? = null
    var userId: String? = null
}

class Ttat {
    var truckNumber: String = ""
    var capacity: String = ""
    var inTime: String = ""
    var outTime: String = ""
    var driver: String = ""
    var loadStartTime: String = ""
    var loadEndTime: String = ""
    var loadingTime: String = ""
    var inOutTime: String = ""
    var idleTime: String = ""
    var createdBy: String? = null
    var updatedBy: String? = null
    var createdAt: String? = null
    var updatdAt: String? = null
}

class Depo {
    var name: String  = ""
    var location: String = ""
    var status: String  = ""
    var createdBy: String? = null
    var updatedBy: String? = null
    var createdAt: String? = null
    var updatdAt: String? = null
}

class DispatchSlipItem {
    var id: Number? = null
    var dispatchSlipId: Number? = null
    var batchNumber: String? = null
    var numberOfPacks: Number = 0
    var materialCode: String? = null
    var createdBy: String? = null
    var updatedBy: String? = null
    var createdAt: String? = null
    var updatdAt: String? = null
    var scannedPacks: Number = 0
}

class DispatchSlipItemRequest {
    var batchNumber: String? = null
    var serialNumber: String? = null
    var materialCode: String? = null
}

class PutPutawayResponse {
    var message: String? = null
}

class PostVendorResponse{
    var message: String? = null

}

class DispatchSlipItemResponse {
    var message: String? = null
}

class DispatchSlipRequest {
    var loadStartTime: Number? = null
    var loadEndTime: Number? = null
    var truckNumber: String? = null
    var dispatchId: Number? = null
    var truckId: Number?  = null
    var materials: Array<DispatchSlipItemRequest>? = null
}

class Project {
    var name: String = ""
    var auditors: String = ""
    var start: String = ""
    var end: String = ""
    var status: Boolean = false
    var projectStatus: String? = null
    var createdBy: String? = null
    var updatedBy: String? = null
    var createdAt: String? = null
    var updatdAt: String? = null
}

class ProjectItem {
    var projectId: Number? = null
    var materialCode: String? = null
    var batchNumber: String? = null
    var serialNumber: String? = null
    var itemStatus: String? = null
}

class PickingItems {
    var rackBarcodeSerial: String? = null
    var binBarcodeSerial: String? = null
    var materialBarcodeSerial: String? = null
}
//
//class PickingRequest{
//    var rackBarcodeSerial: String? = null
//    var binBarcodeSerial: String? = null
//    var materialBarcodeSerial: String? = null
//}
class PutPickingResponse {
    var message: String? = null
}

class AuditItem {
    var materialBarcode: String? = null
    var userId: Number? = null
}
class AuditItemResponse {
    var message: String? = null
}

class PutawayDashboardData{
    var totalCount: Number? = null
    var putawayCount: Number? = null
    var pendingCount: Number? = null
}
class PickingsDashboardData{
    var totalCount: Number? = null
        var pickedCount: Number? = null
    var pendingCount: Number? = null
}

interface ApiInterface {
    @POST("users/sign_in")
    fun login(@Body signInRequest: SignInRequest): Observable<SignInResponse>

    @GET("users")
    fun getUsers(): Observable<Array<User?>>

    @GET("putaways/get/dashboardCount")
    fun getPutawayCount(): Observable<PutawayDashboardData?>

    @GET("pickings/get/dashboardCount")
    fun getPickingsCount(): Observable<PickingsDashboardData?>


    @GET("putaways")
    fun getPutaway(): Observable<Array<PutawayItems?>>

//    @PUT("putaways/{id}")
//    fun putPutawayItems(@Path("id") id: Int, @Body requestbody: PutawayItems): Observable<PutPutawayResponse?>

    @PUT("putaways/1")
    fun putPutawayItems(@Body requestbody: PutawayItems): Observable<PutPutawayResponse?>

    @GET("pickings")
    fun getPickingItems():Observable<Array<PickingItems?>>

    @PUT("pickings/1")
    fun putPickingItems(@Body requestbody: PickingItems): Observable<PutPickingResponse?>

    @GET("materialinwards")
    fun getMaterialDetails(@Query("serialNumber")  serialNumber: String): Observable<Array<MaterialInward>>

    @GET("dispatchslip")
    fun getDispatchSlip(@Path("id") dispatchSlipId: String): Observable<Array<DispatchSlip>>

    @GET("/dispatchpickerrelations/users/{userid}/dispatchslips")
    fun getAssignedPickerDispatchSlips(@Path("userid") userId: Int): Observable<Array<DispatchSlip?>>

    @GET("/dispatchloaderrelations/users/{userid}/dispatchslips")
    fun getAssignedLoaderDispatchSlips(@Path("userid") userId: Int): Observable<Array<DispatchSlip?>>

    @GET("/dispatchslips/{id}/dispatchslipmaterials")
    fun getDispatchSlipMaterials(@Path("id") id: Int): Observable<Array<DispatchSlipItem?>>

    @POST("dispatchslips/{id}/dispatchslippickermaterials")
        fun postDispatchSlipPickedMaterials(@Path("id") id: Int, @Body requestbody: DispatchSlipRequest): Observable<DispatchSlipItemResponse?>

    @POST("dispatchslips/{id}/dispatchsliploadermaterials")
    fun postDispatchSlipLoadedMaterials(@Path("id") id: Int, @Body requestbody: DispatchSlipRequest):
            Observable<DispatchSlipItemResponse?>

    @GET("/projects/{status}")
    fun getAuditProjects(@Path("status") status: String): Observable<Array<Project?>>

    @GET("/project/{id}/projectitems")
    fun getProjectItems(@Path("id") id: String): Observable<Array<ProjectItem?>>

    @POST("/project/{id}/projectitems")
    fun postProjectItems(@Path("id") id: String): Observable<Array<ProjectItem?>>

//    @POST("/materialinwards")
//    fun postMaterialInwards(@Path("userId") userId: String,@Body requestbody: VendorMaterialInward): Observable<Array<VendorMaterialInward?>>

    @POST("/materialinwards")
    fun postMaterialInwards(@Body requestbody: VendorMaterialInward):
            Observable<VendorMaterialInward?>

    @POST("/audits")
    fun postAuditsItems(@Body requestbody: AuditItem): Observable<AuditItemResponse?>
}

