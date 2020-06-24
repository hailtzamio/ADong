package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Worker(
    @SerializedName("address")
    var address: String?,
    @SerializedName("avatarExtId")
    val avatarExtId: String,
    @SerializedName("avatarUrl")
    val avatarUrl: String?,
    @SerializedName("bankAccount")
    var bankAccount: String?,
    @SerializedName("bankName")
    var bankName: String?,
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    val fullName: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isTeamLeader")
    val isTeamLeader: Boolean = false,
    @SerializedName("lineId")
    var lineId: String?,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("phone2")
    var phone2: String?,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String,
    @SerializedName("userFullName")
    val userFullName: String,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("isSelected")
    var isSelected: Boolean = false,
    @SerializedName("workingStatus")
    var workingStatus: String,
    @SerializedName("teamName")
    var teamName: String?
) : Serializable