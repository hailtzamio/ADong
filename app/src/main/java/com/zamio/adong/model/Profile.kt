package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("accountNonExpired")
    val accountNonExpired: Boolean,
    @SerializedName("accountNonLocked")
    val accountNonLocked: Boolean,
    @SerializedName("avatarExtId")
    val avatarExtId: String,
    @SerializedName("avatarUrl")
    val avatarUrl: String?,
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("email")
    val email: String?,
    @SerializedName("enabled")
    val enabled: Boolean,
    @SerializedName("fullName")
    val fullName: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lastUpdate")
    val lastUpdate: String,
    @SerializedName("password")
    val password: Any,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("username")
    val username: String
)