package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Criteria(
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("deleted")
    val deleted: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("key")
    val key: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String
) : Serializable

data class CriteriaMenu(
    var type: String?,
    var score: Float?,
    var key: String?,
    var value: String?,
    var description: String?
)