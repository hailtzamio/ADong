package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductRequirement(
    @SerializedName("code")
    val code: String?,
    @SerializedName("transportReqCode")
    val transportReqCode: String,
    @SerializedName("transportReqId")
    val transportReqId: Int?,
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("expectedDatetime")
    val expectedDatetime: String?,
    @SerializedName("plannedDatetime")
    var plannedDatetime: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("tripId")
    val tripId: Int,
    @SerializedName("tripName")
    var tripName: String?,
    @SerializedName("ref")
    var ref: String?,
    @SerializedName("note")
    var note: String?,
    @SerializedName("warehouseName")
    var warehouseName: String?,
    @SerializedName("warehouseAddress")
    var warehouseAddress: String?,
    @SerializedName("order")
    val order: Int,
    @SerializedName("projectAddress")
    val projectAddress: String,
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("projectName")
    val projectName: String?,
    @SerializedName("status")
    val status: Any,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String,
    @SerializedName("lines")
    val lines: ArrayList<Product>,
    @SerializedName("linesRemove")
    var linesRemove: MutableList<Int>? = null ,
    @SerializedName("linesUpdate")
    var linesUpdate: MutableList<Product>? = null
) : Serializable