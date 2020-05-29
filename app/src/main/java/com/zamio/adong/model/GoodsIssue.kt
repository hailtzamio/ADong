package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GoodsIssue(
    @SerializedName("code")
    val code: String?,
    @SerializedName("confirmationDate")
    val confirmationDate: String?,
    @SerializedName("createdByFullName")
    val createdByFullName: String?,
    @SerializedName("createdById")
    val createdById: Int?,
    @SerializedName("createdTime")
    val createdTime: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lines")
    val lines: ArrayList<GoodsIssueLine>?,
    @SerializedName("note")
    val note: String?,
    @SerializedName("projectId")
    val projectId: Int?,
    @SerializedName("projectName")
    val projectName: String?,
    @SerializedName("reason")
    val reason: String?,
    @SerializedName("receiver")
    val receiver: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String,
    @SerializedName("warehouseId")
    val warehouseId: Int,
    @SerializedName("warehouseName")
    val warehouseName: String?
) :Serializable

data class GoodsIssueLine(
    @SerializedName("createdByFullName")
    val createdByFullName: String?,
    @SerializedName("createdById")
    val createdById: Int?,
    @SerializedName("createdTime")
    val createdTime: String?,
    @SerializedName("docCode")
    val docCode: String?,
    @SerializedName("docId")
    val docId: Int?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("productCode")
    val productCode: String?,
    @SerializedName("productId")
    val productId: Int?,
    @SerializedName("productName")
    val productName: String?,
    @SerializedName("productUnit")
    val productUnit: String?,
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String?,
    @SerializedName("updatedById")
    val updatedById: Int?,
    @SerializedName("updatedTime")
    val updatedTime: String?,
    @SerializedName("warehouseId")
    val warehouseId: Int?,
    @SerializedName("warehouseName")
    val warehouseName: String?
):Serializable

