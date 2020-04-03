package com.zamio.adong.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Worker2(
    val address: String,
    val avatarExtId: String,
    val avatarUrl: String,
    val bankAccount: String,
    val bankName: String,
    val createdByFullName: String,
    val createdById: Int,
    val createdTime: String,
    val email: String,
    val fullName: String,
    val id: Int,
    val isTeamLeader: Boolean,
    val lineId: String,
    val phone: String,
    val phone2: String,
    val teamId: Int,
    val teamName: String,
    val updatedByFullName: String,
    val updatedById: Int,
    val updatedTime: String,
    val userFullName: String,
    val userId: Int,
    @SerializedName("isSelected")
    var isSelected: Boolean = false
) : Parcelable