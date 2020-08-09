


import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


/**
 * Created by admin on 3/8/2018.
 */
interface RestService {


    /*Login*/
    @POST("user/login-by-facebook")
    @FormUrlEncoded
    abstract fun loginWithFacebook(@Field("facebook_id") feedId: String,@Field("access_token") id: String): Call<RestData<JsonElement>>


    @POST("login")
    fun login(@Body user: JsonObject): Call<User>

    @PUT("changeMyPassword")
    fun changeMyPassword(@Body user: JsonObject): Call<JsonElement>

    @PUT("updateMyProfile")
    fun updateMyProfile(@Body user: JsonObject): Call<JsonElement>

    @GET("myProfile")
    fun getProfile(): Call<RestData<Profile>>

    @GET("myPermissions")
    fun getPermissions(): Call<RestData<ArrayList<Permission>>>

    @GET("product?size=100&sort=id,desc")
    abstract fun getProducts(@Query("page") page: Int,@Query("name") fullName: String): Call<RestData<ArrayList<Product>>>

    @GET("product/{id}")
    fun getProduct(@Path("id") productId: Int): Call<RestData<Product>>

    @GET("notification/{id}")
    fun getNotification(@Path("id") productId: Int): Call<RestData<JsonElement>>

    @POST("product")
    fun createUser(@Body product: JsonObject): Call<RestData<JsonElement>>

    @DELETE("product/{id}")
    fun removeProduct(@Path("id") productId: Int): Call<RestData<JsonElement>>

    @PUT("product/{id}")
    fun updateProduct(@Path("id") id: Int, @Body lorry: JsonObject): Call<RestData<JsonElement>>


    @GET("lorry?size=100&sort=id,desc")
    abstract fun getLorries(): Call<RestData<List<Lorry>>>

    @GET("lorry/{id}")
    fun getLorry(@Path("id") lorryId: Int): Call<RestData<Lorry>>

    @POST("lorry")
    fun createLorry(@Body product: JsonObject): Call<RestData<JsonElement>>

    @DELETE("lorry/{id}")
    fun removeLorry(@Path("id") lorryId: Int): Call<RestData<JsonElement>>

    @PUT("lorry/{id}")
    fun updateLorry(@Path("id") id: Int, @Body lorry: JsonObject): Call<RestData<JsonElement>>


    @Multipart
    @POST("uploadAvatar")
    fun updateProfile(@Part image: MultipartBody.Part): Call<RestData<JsonElement>>

    /* Woker */
    @GET("worker?size=100&sort=id,desc")
    fun getWorkers(@Query("page") page: Int,@Query("fullName") fullName: String): Call<RestData<ArrayList<Worker>>>

    @GET("worker?isTeamLeader=false&size=100&sort=id,desc")
    fun getWorkersNotLeaders(@Query("page") page: Int,@Query("fullName") fullName: String): Call<RestData<List<Worker>>>

    @GET("worker?inTeam=false&size=100")
    fun getWorkers2(@Query("page") page: Int,@Query("fullName") fullName: String): Call<RestData<ArrayList<Worker2>>>

    @GET("worker/{id}")
    fun getWorker(@Path("id") lorryId: Int): Call<RestData<Worker>>

    @PUT("worker/{id}")
    fun updateWorker(@Path("id") id: Int, @Body lorry: JsonObject): Call<RestData<JsonElement>>

    @POST("worker")
    fun createWorker(@Body worker: JsonObject): Call<RestData<JsonElement>>

    @DELETE("worker/{id}")
    fun removeWorker(@Path("id") productId: Int): Call<RestData<JsonElement>>
    /* End Woker */

    /* Criteria */

    @GET("criteriaBundle?size=100&sort=id,desc")
    fun getCriterias(@Query("page") page: Int,@Query("name") fullName: String): Call<RestData<List<Criteria>>>

    @GET("sysParam?size=100&sort=id,desc")
    fun getSysParams(): Call<RestData<ArrayList<CriteriaMenu>>>

    @GET("criteriaBundle/{cbId}")
    fun getCriteria(@Path("cbId") id: Int): Call<RestData<Criteria>>

    @PUT("criteriaBundle/{cbId}")
    fun updateCriteria(@Path("cbId") id: Int, @Body lorry: UpdateCriteria): Call<RestData<JsonElement>>

    @POST("criteriaBundle")
    fun createCriteria(@Body data: CriteriaValues): Call<RestData<JsonElement>>

    @DELETE("criteriaBundle/{cbId}")
    fun removeCriteria(@Path("cbId") id: Int): Call<RestData<JsonElement>>


    @GET("criteriaBundle/{cbId}/criteria")
    fun getSmallCriteria(@Path("cbId") id: Int): Call<RestData<ArrayList<CriteriaSmall>>>

    /* End Criteria */

    /* Team */
    @GET("team?size=100&sort=id,desc")
    fun getTeams(@Query("page") page: Int, @Query("name") name: String): Call<RestData<List<Team>>>

    @GET("team/{id}")
    fun getTeam(@Path("id") teamId: Int): Call<RestData<Team>>

    @GET("team/{teamId}/members")
    fun getWorkerFromTeam(@Path("teamId") teamId: Int): Call<RestData<ArrayList<Worker2>>>

    @DELETE("team/{id}")
    fun removeTeam(@Path("id") lorryId: Int): Call<RestData<JsonElement>>

    @POST("team")
    fun createTeam(@Body product: JsonObject): Call<RestData<JsonElement>>

    @PUT("team/{id}")
    fun updateTeam(@Path("id") id: Int, @Body objectJs: JsonObject): Call<RestData<JsonElement>>

    @GET("worker?isTeamLeader=true")
    fun getTeamLeaders(@Query("page") page: Int,@Query("name") name: String): Call<RestData<List<Worker>>>

    @GET("province?size=100")
    fun getProvince(): Call<RestData<ArrayList<Province>>>

    @GET("province/{provinceId}/districts?size=100")
    fun getDistricts(@Path("provinceId") provinceId: Int): Call<RestData<ArrayList<Province>>>

    /* End Team */

    /* Driver */

    @GET("driver?size=100&sort=id,desc")
    fun getDrivers(@Query("page") page: Int,@Query("fullName") fullName: String): Call<RestData<List<Driver>>>

    @POST("driver")
    fun createDriver(@Body worker: JsonObject): Call<RestData<JsonElement>>

    @GET("driver/{id}")
    fun getDriver(@Path("id") lorryId: Int): Call<RestData<Driver>>

    @DELETE("driver/{id}")
    fun removeDriver(@Path("id") productId: Int): Call<RestData<JsonElement>>

    @PUT("driver/{id}")
    fun updateDriver(@Path("id") id: Int, @Body lorry: JsonObject): Call<RestData<JsonElement>>
    /* End Driver */

    /* Contructor */
    @GET("contractor?size=100&sort=id,desc")
    fun getContractors(@Query("page") page: Int,@Query("name") fullName: String): Call<RestData<List<Contractor>>>

    @POST("contractor")
    fun createContractor(@Body worker: JsonObject): Call<RestData<JsonElement>>

    @GET("contractor/{id}")
    fun getContractor(@Path("id") lorryId: Int): Call<RestData<Contractor>>

    @DELETE("contractor/{id}")
    fun removeContractor(@Path("id") productId: Int): Call<RestData<JsonElement>>

    @PUT("contractor/{id}")
    fun updateContractor(@Path("id") id: Int, @Body lorry: JsonObject): Call<RestData<JsonElement>>


    /* End Contructor */


    /* Project */

    @POST("project")
    fun createProject(@Body worker: JsonObject): Call<RestData<JsonElement>>

    @PUT("project/{id}")
    fun updateProjcet(@Path("id") productId: Int,@Body worker: JsonObject): Call<RestData<JsonElement>>

    @DELETE("project/{id}")
    fun removeProject(@Path("id") productId: Int): Call<RestData<JsonElement>>

    @PUT("project/{id}/pause")
    fun pauseProject(@Path("id") productId: Int,@Body worker: JsonObject): Call<RestData<JsonElement>>

    @GET("project/{id}/markSessions")
    fun getMarkSessions(@Path("id") productId: Int): Call<RestData<ArrayList<MarkSession>>>

    @PUT("project/{id}/resume")
    fun pauseResume(@Path("id") productId: Int,@Body worker: JsonObject): Call<RestData<JsonElement>>

    @GET("project?size=50&sort=id,desc")
    fun getProjects(@Query("page") page: Int,@Query("search") fullName: String,@Query("status") status: String): Call<RestData<List<Project>>>

    @GET("project/forMap?size=50&sort=id,desc")
    fun getProjectsMap(@Query("page") page: Int,@Query("search") fullName: String,@Query("status") status: String): Call<RestData<ArrayList<Project>>>

    @GET("project/{id}")
    fun getProject(@Path("id") id: Int): Call<RestData<Project>>

    @GET("user/{id}")
    fun getUer(@Path("id") id: Int): Call<RestData<Profile>>

    @GET("user?size=100&sort=id,desc&authorityCode=MANAGER")
    fun getManagers(@Query("page") page: Int,@Query("fullname") name: String): Call<RestData<List<Worker>>>

    @GET("user?size=100&sort=id,desc&authorityCode=SECRETARY")
    fun getSecretaries(@Query("page") page: Int,@Query("fullname") name: String): Call<RestData<List<Worker>>>

    @GET("user?size=100&sort=id,desc&authorityCode=DEPUTY_MANAGER")
    fun getDeputyManagers(@Query("page") page: Int,@Query("fullname") name: String): Call<RestData<List<Worker>>>

    @GET("user?size=100&sort=id,desc&authorityCode=AREA_MANAGER")
    fun geAreaManagers(@Query("page") page: Int,@Query("fullname") name: String): Call<RestData<List<Worker>>>

    @POST("checkin")
    fun checkin(@Body worker: CheckinOut): Call<RestData<JsonElement>>

    @POST("checkout")
    fun checkout(@Body worker: CheckinOut): Call<RestData<JsonElement>>

    /* End Project */
    /* WorkOutline  */

    @GET("productRequirement/{id}/goodsIssueRequests")
    fun getProductRequirementGoodsIssue(@Path("id") id: Int,@Query("productRequirementId") id2: Int): Call<RestData<ArrayList<ProductRequirement>>>

    @GET("productRequirement/{id}/purchaseRequests")
    fun getProductRequirementpurchaseRequests(@Path("id") id: Int,@Query("productRequirementId") id2: Int): Call<RestData<ArrayList<ProductRequirement>>>

    @GET("project/{projectId}/workOutlines?size=100&sort=id,desc")
    fun getProjectWorkOutlines(@Path("projectId") projectId: Int, @Query("page") page: Int): Call<RestData<List<WorkOutline>>>

    @GET("project/{projectId}/productRequirements?size=100&sort=id,desc")
    fun getProductRequirement(@Path("projectId") projectId: Int,@Query("page") page: Int): Call<RestData<ArrayList<ProductRequirement>>>

    @GET("project/{projectId}/workers?size=100&sort=id,desc")
    fun getProjectWorkers(@Path("projectId") projectId: Int,@Query("page") page: Int): Call<RestData<List<Worker>>>

    @GET("workOutline?size=100&sort=id,desc")
    fun getWorkOutlines(@Query("page") page: Int,@Query("name") fullName: String): Call<RestData<List<WorkOutline>>>

    @GET("workOutline/{id}")
    fun getWorkOutline(@Path("id") id: Int): Call<RestData<WorkOutline>>

    @DELETE("workOutline/{id}")
    fun removeWorkOutline(@Path("id") id: Int): Call<RestData<JsonElement>>

    @PUT("workOutline/{id}")
    fun updateWorkOutline(@Path("id") id: Int, @Body lorry: JsonObject): Call<RestData<JsonElement>>

    @POST("workOutline")
    fun createWorkOutline(@Body data: JsonObject): Call<RestData<JsonElement>>

    @POST("project/{projectId}/productRequirement")
    fun createProductRequirementForProject(@Body data: ProductRequirementRes,@Path("projectId") id: Int): Call<RestData<JsonElement>>

    @GET("project/{projectId}/workers?size=100&sort=id,desc")
    fun getProjectWorker(@Path("projectId") id: Int,@Query("name") fullName: String): Call<RestData<List<Worker>>>

    @POST("project/{projectId}/addWorker")
    fun addWorkerToProject(@Path("projectId") id: Int, @Body data: JsonObject): Call<RestData<JsonElement>>

    @Multipart
    @POST("project/{projectId}/uploadCheckinPhoto")
    fun updateImageCheckin(@Path("projectId") id: Int,@Part image: MultipartBody.Part): Call<RestData<JsonElement>>

    @GET("project/{projectId}/uploadSessions")
    fun getProjectFiles(@Path("projectId") id: Int): Call<RestData<List<Project>>>


    @GET("project/{projectId}/checkinPhotos")
    fun getProjectImages(@Path("projectId") id: Int): Call<RestData<ArrayList<ProjectImage>>>

    @GET("project/{projectId}/projectCompletionPhotos")
    fun getProjectFinishImages(@Path("projectId") id: Int): Call<RestData<ArrayList<ProjectImage>>>

    @GET("project/{projectId}/attendances")
    fun getProjectAttendances(@Path("projectId") id: Int): Call<RestData<List<AttendanceCheckout>>>

    @PUT("projectWorkOutline/{projectWorkOutlineId}/finish")
    fun finishProjectWorkOutlines(@Path("projectWorkOutlineId") id: Int): Call<RestData<JsonElement>>

    @Multipart
    @POST("projectWorkOutline/{projectWorkOutlineId}/uploadCompletionPhoto")
    fun finishImageProjectWorkOutlines(@Path("projectWorkOutlineId") id: Int, @Part image: MultipartBody.Part): Call<RestData<JsonElement>>

    @Multipart
    @POST("project/{projectId}/uploadProjectCompletionPhoto")
    fun finishImageProjectConpletion(@Path("projectId") id: Int, @Part image: MultipartBody.Part): Call<RestData<JsonElement>>

    @PUT("project/{projectId}/finish")
    fun finishProject(@Path("projectId") id: Int): Call<RestData<JsonElement>>

    // WareHouse
    @GET("warehouse")
    fun getStocks(@Query("name") name: String, @Query("type") page: String): Call<RestData<ArrayList<WareHouse>>>

    @GET("warehouse?type=FACTORY")
    fun getFactories(@Query("name") name: String): Call<RestData<List<WareHouse>>>

    @POST("warehouse?size=100&sort=id,desc")
    fun createWareHouse(@Body worker: JsonObject): Call<RestData<JsonElement>>

    @GET("warehouse/{id}")
    fun getWareHouse(@Path("id") id: Int): Call<RestData<WareHouse>>

    @GET("goodsReceivedNote?size=100&sort=id,desc")
    fun getGoodsReceivedNotes(@Query("page") page: Int,@Query("warehouseId") id: Int): Call<RestData<ArrayList<GoodsNote>>>

    @POST("goodsReceivedNote")
    fun createGoodsReceivedNote(@Body data: GoodsNoteRq): Call<RestData<JsonElement>>

    @GET("goodsReceivedNote/{id}")
    fun getGoodsReceivedNote(@Path("id") id: Int): Call<RestData<GoodsNote>>

    @PUT("goodsReceivedNote/{id}")
    fun updateGoodsReceivedNote(@Path("id") id: Int,@Body data: GoodsNoteUpdateRq): Call<RestData<JsonElement>>

    @PUT("goodsReceivedNote/{id}/confirm")
    fun confirmGoodsReceivedNote(@Path("id") id: Int): Call<RestData<JsonElement>>


    // Good Issue
    @GET("goodsIssueDocument?size=100&sort=id,desc")
    fun getGoodsIssues(@Query("page") page: Int , @Query("warehouseId") id: Int): Call<RestData<ArrayList<GoodsIssue>>>

    @POST("goodsIssueDocument")
    fun createGoodsIssueDocument(@Body data: GoodsNoteUpdateRq): Call<RestData<JsonElement>>

    @GET("goodsIssueDocument/{id}")
    fun getGoodsIssueDocument(@Path("id") id: Int): Call<RestData<GoodsIssue>>

    @PUT("goodsIssueDocument/{id}")
    fun updateGoodsIssueDocument(@Path("id") id: Int, @Body data: GoodsNoteUpdateRq): Call<RestData<JsonElement>>

    @PUT("goodsIssueDocument/{id}/confirm")
    fun confirmGoodsIssueDocument(@Path("id") grnId: Int): Call<RestData<JsonElement>>

    // ManuFacture
    @GET("manufactureRequest?size=100&sort=id,desc")
    fun getManufactures(@Query("page") page: Int, @Query("warehouseId") id: Int): Call<RestData<ArrayList<ManuFactureRes>>>

    @GET("manufactureRequest/{id}")
    fun getManufacture(@Path("id") id: Int): Call<RestData<ManuFactureRes>>

    @PUT("manufactureRequest/{id}/confirm")
    fun confirmManufacture(@Path("id") grnId: Int): Call<RestData<JsonElement>>

    @PUT("manufactureRequest/{id}")
    fun updateManufacture(@Path("id") id: Int, @Body data: GoodsNoteUpdateRq): Call<RestData<JsonElement>>

    // Goods Issue Request
    @GET("goodsIssueRequest?size=100&sort=id,desc")
    fun getGoodsIssueRequests(@Query("page") page: Int , @Query("warehouseId") id: Int): Call<RestData<ArrayList<GoodsIssueRequest>>>



    @POST("goodsIssueRequest")
    fun createGoodsIssueRequest(@Body data: GoodsNoteUpdateRq2): Call<RestData<JsonElement>>

    @POST("manufactureRequest")
    fun createManufactureRequest(@Body data: GoodsNoteUpdateRq2): Call<RestData<JsonElement>>


    @POST("purchaseRequest")
    fun createPurchaseRequest(@Body data: GoodsNoteUpdateRq2): Call<RestData<JsonElement>>

    @GET("goodsIssueRequest/{id}")
    fun getGoodsIssueRequest(@Path("id") id: Int): Call<RestData<GoodsIssueRequest>>

    @PUT("goodsIssueRequest/{id}")
    fun updateGoodsIssueRequest(@Path("id") id: Int, @Body data: GoodsNoteUpdateRq): Call<RestData<JsonElement>>

    @DELETE("workOutlineCompletionPhoto/{photoId}")
    fun removeWorkOutlineCompletionPhoto(@Path("photoId") id: Int): Call<RestData<JsonElement>>

    @DELETE("goodsIssueRequest/{id}")
    fun removeGoodsIssueRequest(@Path("id") id: Int): Call<RestData<JsonElement>>

    @GET("user?size=100&sort=id,desc&authorityCode=WAREHOUSE_KEEPER")
    fun getkeepers(@Query("page") page: Int,@Query("fullname") name: String): Call<RestData<List<Worker>>>

    @GET("user?size=100&sort=id")
    fun getBuyers(@Query("page") page: Int,@Query("fullname") name: String): Call<RestData<List<Worker>>>

    @GET("transportRequest?status=1&size=100&sort=id,desc")
    abstract fun getTransports(@Query("page") page: Int,@Query("name") fullName: String): Call<RestData<ArrayList<Transport>>>

    @GET("project/{id}/logs")
    abstract fun getProjectLogs(@Path("id") id: Int): Call<RestData<ArrayList<LogsProject>>>

    @GET("transportRequest/{id}")
    fun getTransport(@Path("id") id: Int): Call<RestData<Transport>>

    @Multipart
    @POST("transportRequest/{id}/uploadPhoto")
    fun transportUpload(@Path("id") id: Int, @Part image: MultipartBody.Part): Call<RestData<JsonElement>>

    @PUT("transportRequest/{id}/pickup")
    fun transportPickUp(@Path("id") id: Int): Call<RestData<JsonElement>>

    @PUT("transportRequest/{id}/unload")
    fun transportUnload(@Path("id") id: Int): Call<RestData<JsonElement>>

    @GET("trip?size=100&sort=id,desc")
    abstract fun getTrips(@Query("page") page: Int,@Query("name") fullName: String): Call<RestData<ArrayList<Trip>>>

    @GET("trip?size=100&sort=id,desc&status=1")
    abstract fun getTripsNew(@Query("page") page: Int,@Query("name") fullName: String): Call<RestData<ArrayList<Trip>>>

    @POST("trip")
    fun createTrip(@Body data: TripRq): Call<RestData<JsonElement>>

    @GET("trip/{id}")
    fun getTrip(@Path("id") id: Int): Call<RestData<Trip>>

    @GET("trip/{id}/photos")
    fun getTripImages(@Path("id") id: Int): Call<RestData<List<ProjectImage>>>


    @GET("project/registrable?size=100&sort=id,desc")
    abstract fun getProjectRegistrable(): Call<RestData<ArrayList<Project>>>

    @GET("registration?size=100&sort=id,desc")
    abstract fun getProjectRegister(@Query("projectId") id: Int): Call<RestData<ArrayList<Contractor>>>

    @POST("project/{id}/register")
    fun registerProject(@Path("id") id: Int,@Body data: JsonObject): Call<RestData<JsonElement>>

    @PUT("registration/{id}/approve")
    fun approveRegisterProject(@Path("id") id: Int): Call<RestData<JsonElement>>

    @GET("registration/{id}")
    abstract fun getRegistration(@Path("id") id: Int): Call<RestData<Project>>

    @GET("myRoles")
    abstract fun getMyRoles(): Call<RestData<ArrayList<User>>>

    @GET("myNotifications?size=100&sort=id,desc")
    fun getNotifications(@Query("page") page: Int,@Query("fullName") fullName: String): Call<RestData<ArrayList<NotificationOb>>>

    @GET("myNotifications/countNotSeen")
    fun getNotificationCount(): Call<RestData<NotificationOb>>

    @GET("isRegistered")
    abstract fun checkHideShowRegistrationButton(@Query("contractorId") contractorId: Int,@Query("projectId") projectId: Int): Call<RestData<Contractor>>

}