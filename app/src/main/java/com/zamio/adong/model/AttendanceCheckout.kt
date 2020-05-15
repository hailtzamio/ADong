package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class AttendanceCheckout(
    @SerializedName("checkinTime")
    val checkinTime: String?,
    @SerializedName("checkoutTime")
    val checkoutTime: String?,
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("projectAddress")
    val projectAddress: String,
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("projectName")
    val projectName: String,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String,
    @SerializedName("workerFullName")
    val workerFullName: String,
    @SerializedName("workerId")
    val workerId: Int
)