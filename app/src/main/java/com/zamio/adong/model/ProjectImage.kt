package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class ProjectImage(
    @SerializedName("cameraModelName")
    val cameraModelName: String,
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("dateTimeOriginal")
    val dateTimeOriginal: String,
    @SerializedName("deviceManufacturer")
    val deviceManufacturer: String,
    @SerializedName("deviceModel")
    val deviceModel: String,
    @SerializedName("fullSizeFileExternalId")
    val fullSizeFileExternalId: String,
    @SerializedName("fullSizeFileId")
    val fullSizeFileId: Int,
    @SerializedName("fullSizeUrl")
    val fullSizeUrl: String?,
    @SerializedName("fullSizeFileUrl")
    val fullSizeFileUrl: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("latitude")
    val latitude: Int,
    @SerializedName("lensMake")
    val lensMake: String,
    @SerializedName("lensModel")
    val lensModel: String,
    @SerializedName("longitude")
    val longitude: Int,
    @SerializedName("originalName")
    val originalName: String,
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("projectName")
    val projectName: String,
    @SerializedName("thumbnailExternalId")
    val thumbnailExternalId: String,
    @SerializedName("thumbnailId")
    val thumbnailId: Int,
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String?,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String
)