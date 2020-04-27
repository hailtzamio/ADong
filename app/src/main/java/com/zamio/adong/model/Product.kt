package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Product(
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String,
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String?,
    @SerializedName("thumbnailExtId")
    val thumbnailExtId: String
) : Serializable