package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Lorry (
    @SerializedName("address")
    val address: Any,
    @SerializedName("brand")
    val brand: String,
    @SerializedName("capacity")
    val capacity: String?,
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("driverFullName")
    val driverFullName: String?,
    @SerializedName("driverId")
    val driverId: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("model")
    val model: String,
    @SerializedName("plateNumber")
    val plateNumber: String?,
    @SerializedName("status")
    val status: Any,
    @SerializedName("tripId")
    val tripId: Any,
    @SerializedName("tripName")
    val tripName: Any,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String
) : Serializable