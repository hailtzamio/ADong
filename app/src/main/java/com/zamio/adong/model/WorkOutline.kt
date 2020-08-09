package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WorkOutline(
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("sequence")
    val sequence: Int,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String,
    @SerializedName("projectName")
    val projectName: String,
    @SerializedName("workOutlineName")
    val workOutlineName: String,
    @SerializedName("finishDatetime")
    var finishDatetime: String?,
    @SerializedName("order")
    val order: Int,
    @SerializedName("photos")
    val photos: List<WorkOutlineImage>?

) : Serializable

data class WorkOutlineImage(
    @SerializedName("fullSizeUrl")
    val fullSizeUrl: String,
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String,
    @SerializedName("photoId")
    val photoId: Int
)