package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Driver(
    @SerializedName("avatarUrl")
    val avatarUrl: String? = null,
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lorryId")
    val lorryId: Int,
    @SerializedName("lorryPlateNumber")
    val lorryPlateNumber: String,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("phone2")
    val phone2: String?,
    @SerializedName("tripId")
    val tripId: Int,
    @SerializedName("tripName")
    val tripName: String,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String,
    @SerializedName("workingStatus")
    val workingStatus: String,
    @SerializedName("email")
    val email: String?,
    @SerializedName("avatarExtId")
    val avatarExtId: String
) : Serializable