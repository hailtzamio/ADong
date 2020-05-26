package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class WareHouse(
    @SerializedName("address")
    val address: String,
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("keeperFullName")
    val keeperFullName: String,
    @SerializedName("keeperId")
    val keeperId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String
)