package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductRequirement(
    @SerializedName("code")
    val code: String,
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("expectedDatetime")
    val expectedDatetime: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("note")
    val note: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("projectAddress")
    val projectAddress: String,
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("projectName")
    val projectName: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String,
    @SerializedName("lines")
    val lines: List<Product>
) : Serializable