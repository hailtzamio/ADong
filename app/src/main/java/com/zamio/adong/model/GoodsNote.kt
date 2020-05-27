package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class GoodsNote(
    @SerializedName("code")
    val code: String,
    @SerializedName("confirmationDate")
    val confirmationDate: String,
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("deliveredBy")
    val deliveredBy: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lines")
    val lines: ArrayList<String>,
    @SerializedName("note")
    val note: String,
    @SerializedName("ref")
    val ref: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String,
    @SerializedName("warehouseId")
    val warehouseId: Int,
    @SerializedName("warehouseName")
    val warehouseName: String
)