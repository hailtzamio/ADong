package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class SmallCriteria(
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("criteriaBundleId")
    val criteriaBundleId: Int,
    @SerializedName("criteriaBundleName")
    val criteriaBundleName: String,
    @SerializedName("factor")
    val factor: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String
)