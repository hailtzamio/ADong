package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FileProject(
    @SerializedName("createdByFullName")
    var createdByFullName: String?,
    @SerializedName("createdById")
    var createdById: Int?,
    @SerializedName("createdTime")
    var createdTime: String?,
    @SerializedName("downloadUrl")
    var downloadUrl: String?,
    @SerializedName("extId")
    var extId: String?,
    @SerializedName("fileName")
    var fileName: String?,
    @SerializedName("fileSize")
    var fileSize: Int?,
    @SerializedName("fileSizeHuman")
    var fileSizeHuman: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("updatedByFullName")
    var updatedByFullName: String?,
    @SerializedName("updatedById")
    var updatedById: Int?,
    @SerializedName("updatedTime")
    var updatedTime: String?,
    @SerializedName("uploadSessionId")
    var uploadSessionId: Int?
) : Serializable