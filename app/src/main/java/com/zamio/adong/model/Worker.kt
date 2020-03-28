package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Worker(
    @SerializedName("address")
    val address: String,
    @SerializedName("avatarExtId")
    val avatarExtId: String,
    @SerializedName("avatarUrl")
    val avatarUrl: String,
    @SerializedName("bankAccount")
    val bankAccount: String,
    @SerializedName("bankName")
    val bankName: String,
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isTeamLeader")
    val isTeamLeader: Boolean,
    @SerializedName("lineId")
    val lineId: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("phone2")
    val phone2: String,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String,
    @SerializedName("userFullName")
    val userFullName: String,
    @SerializedName("userId")
    val userId: Int
) : Serializable