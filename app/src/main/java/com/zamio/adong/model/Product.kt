package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Product(
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("code")
    val code: String?,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("quantity")
    var quantity: Double,
    @SerializedName("type")
    val type: String,
    @SerializedName("unit")
    val unit: String?,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String,
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String?,
    @SerializedName("thumbnailExtId")
    val thumbnailExtId: String,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("productUnit")
    val productUnit: String,
    @SerializedName("note")
    var note: String?,
    @SerializedName("productId")
    val productId: Int,
    @SerializedName("weight")
    val weight: Double?,
    @SerializedName("length")
    val length: Double?,
    @SerializedName("width")
    val width: Double?,
    @SerializedName("height")
    val height: Double?,
    @SerializedName("isSelected")
    var isSelected: Boolean = false,
    @SerializedName("quantityChoose")
    var quantityChoose: Int,
    @SerializedName("statusText")
    var statusText: String?

) : Serializable