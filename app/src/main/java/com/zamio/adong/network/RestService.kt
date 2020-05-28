


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

    @GET("myProfile")
    fun getProfile(): Call<RestData<Profile>>

    @GET("myPermissions")
    fun getPermissions(): Call<RestData<ArrayList<Permission>>>

    @GET("product?size=1000&sort=id,desc")
    abstract fun getProducts(@Query("page") page: Int,@Query("name") fullName: String): Call<RestData<ArrayList<Product>>>

    @GET("product/{id}")
    fun getProduct(@Path("id") productId: Int): Call<RestData<Product>>

    @POST("product")
    fun createUser(@Body product: JsonObject): Call<RestData<JsonElement>>

    @DELETE("product/{id}")
    fun removeProduct(@Path("id") productId: Int): Call<RestData<JsonElement>>

    @PUT("product/{id}")
    fun updateProduct(@Path("id") id: Int, @Body lorry: JsonObject): Call<RestData<JsonElement>>


    @GET("lorry?size=1000&sort=id,desc")
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

    @GET("worker?inTeam=false&size=1000")
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

    @GET("criteriaBundle?size=1000&sort=id,desc")
    fun getCriterias(@Query("page") page: Int,@Query("name") fullName: String): Call<RestData<List<Criteria>>>

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
    @GET("team?size=1000&sort=id,desc")
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

    @GET("province?size=1000")
    fun getProvince(): Call<RestData<ArrayList<Province>>>

    @GET("province/{provinceId}/districts?size=1000")
    fun getDistricts(@Path("provinceId") provinceId: Int): Call<RestData<ArrayList<Province>>>

    /* End Team */

    /* Driver */

    @GET("driver?size=1000&sort=id,desc")
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
    @GET("contractor?size=1000&sort=id,desc")
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

    @PUT("project/{id}/resume")
    fun pauseResume(@Path("id") productId: Int,@Body worker: JsonObject): Call<RestData<JsonElement>>

    @GET("project?size=50&sort=id,desc")
    fun getProjects(@Query("page") page: Int,@Query("search") fullName: String): Call<RestData<List<Project>>>

    @GET("project/{id}")
    fun getProject(@Path("id") id: Int): Call<RestData<Project>>

    @GET("user?size=1000&sort=id,desc&authorityCode=MANAGER")
    fun getManagers(@Query("page") page: Int,@Query("fullname") name: String): Call<RestData<List<Worker>>>

    @GET("user?size=1000&sort=id,desc&authorityCode=SECRETARY")
    fun getSecretaries(@Query("page") page: Int,@Query("fullname") name: String): Call<RestData<List<Worker>>>

    @GET("user?size=1000&sort=id,desc&authorityCode=DEPUTY_MANAGER")
    fun getDeputyManagers(@Query("page") page: Int,@Query("fullname") name: String): Call<RestData<List<Worker>>>

    @POST("checkin")
    fun checkin(@Body worker: CheckinOut): Call<RestData<JsonElement>>

    @POST("checkout")
    fun checkout(@Body worker: CheckinOut): Call<RestData<JsonElement>>

    /* End Project */
    /* WorkOutline  */

    @GET("project/{projectId}/workOutlines?size=1000&sort=id,desc")
    fun getProjectWorkOutlines(@Path("projectId") projectId: Int, @Query("page") page: Int): Call<RestData<List<WorkOutline>>>

    @GET("project/{projectId}/productRequirements?size=1000&sort=id,desc")
    fun getProductRequirement(@Path("projectId") projectId: Int,@Query("page") page: Int): Call<RestData<ArrayList<ProductRequirement>>>

    @GET("project/{projectId}/workers?size=100&sort=id,desc")
    fun getProjectWorkers(@Path("projectId") projectId: Int,@Query("page") page: Int): Call<RestData<List<Worker>>>

    @GET("workOutline?size=1000&sort=id,desc")
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


    @GET("project/{projectId}/workers?size=1000&sort=id,desc")
    fun getProjectWorker(@Path("projectId") id: Int,@Query("name") fullName: String): Call<RestData<List<Worker>>>

    @POST("project/{projectId}/addWorker")
    fun addWorkerToProject(@Path("projectId") id: Int, @Body data: JsonObject): Call<RestData<JsonElement>>

    @Multipart
    @POST("project/{projectId}/uploadCheckinPhoto")
    fun updateImageCheckin(@Path("projectId") id: Int,@Part image: MultipartBody.Part): Call<RestData<JsonElement>>

    @GET("project/{projectId}/checkinPhotos")
    fun getProjectImages(@Path("projectId") id: Int): Call<RestData<List<ProjectImage>>>

    @GET("project/{projectId}/attendances")
    fun getProjectAttendances(@Path("projectId") id: Int): Call<RestData<List<AttendanceCheckout>>>

    @PUT("projectWorkOutline/{projectWorkOutlineId}/finish")
    fun finishProjectWorkOutlines(@Path("projectWorkOutlineId") id: Int): Call<RestData<JsonElement>>

    @Multipart
    @POST("projectWorkOutline/{projectWorkOutlineId}/uploadCompletionPhoto")
    fun finishImageProjectWorkOutlines(@Path("projectWorkOutlineId") id: Int, @Part image: MultipartBody.Part): Call<RestData<JsonElement>>

    // WareHouse
    @GET("warehouse?type=STOCK")
    fun getStocks(@Query("name") name: String): Call<RestData<ArrayList<WareHouse>>>

    @GET("warehouse?type=FACTORY")
    fun getFactories(@Query("name") name: String): Call<RestData<List<WareHouse>>>

    @POST("warehouse?size=100&sort=id,desc")
    fun createWareHouse(@Body worker: JsonObject): Call<RestData<JsonElement>>

    @GET("warehouse/{id}")
    fun getWareHouse(@Path("id") id: Int): Call<RestData<WareHouse>>

    @GET("goodsReceivedNote?size=100&sort=id,desc")
    fun getGoodsReceivedNotes(@Query("page") page: Int): Call<RestData<ArrayList<GoodsNote>>>

    @POST("goodsReceivedNote")
    fun createGoodsReceivedNote(@Body data: GoodsNoteRq): Call<RestData<JsonElement>>

    @GET("goodsReceivedNote/{id}")
    fun getGoodsReceivedNote(@Path("id") id: Int): Call<RestData<GoodsNote>>

    @PUT("goodsReceivedNote/{id}")
    fun updateGoodsReceivedNote(@Path("id") id: Int,@Body data: GoodsNoteUpdateRq): Call<RestData<JsonElement>>

    @PUT("goodsReceivedNote/{id }/confirm")
    fun confirmGoodsReceivedNote(@Query("id") grnId: Int): Call<RestData<JsonElement>>

    @GET("goodsIssueDocument")
    fun getGoodsIssueDocuments(@Query("id") id: Int): Call<RestData<List<GoodsNote>>>

    @POST("goodsIssueDocument")
    fun createGoodsIssueDocument(@Body data: GoodsNote): Call<RestData<JsonElement>>

    @GET("goodsIssueDocument/{id}")
    fun getGoodsIssueDocument(@Query("id") id: Int): Call<RestData<GoodsNote>>

    @PUT("goodsIssueDocument")
    fun updateGoodsIssueDocument(@Body data: GoodsNote): Call<RestData<JsonElement>>

    @PUT("goodsIssueDocument/{id }/confirm")
    fun confirmGoodsIssueDocument(@Query("id") grnId: Int): Call<RestData<JsonElement>>


    @GET("goodsIssueRequest")
    fun getGoodsIssueRequests(@Query("id") id: Int): Call<RestData<List<GoodsIssueRequest>>>

    @POST("goodsIssueRequest")
    fun createGoodsIssueRequest(@Body data: GoodsIssueRequest): Call<RestData<JsonElement>>

    @GET("goodsIssueRequest")
    fun getGoodsIssueRequest(@Query("id") id: Int): Call<RestData<GoodsIssueRequest>>

    @PUT("goodsIssueRequest")
    fun updateGoodsIssueRequest(@Body data: GoodsIssueRequest): Call<RestData<JsonElement>>

    @GET("user?size=1000&sort=id,desc&authorityCode=WAREHOUSE_KEEPER")
    fun getkeepers(@Query("page") page: Int,@Query("fullname") name: String): Call<RestData<List<Worker>>>
}