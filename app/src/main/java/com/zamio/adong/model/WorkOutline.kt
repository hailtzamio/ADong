package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WorkOutline(
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("sequence")
    val sequence: Int,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String,
    @SerializedName("projectName")
    val projectName: String,
    @SerializedName("workOutlineName")
    val workOutlineName: String,
    @SerializedName("finishDatetime")
    val finishDatetime: String,
    @SerializedName("order")
    val order: Int
) : Serializable