package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("avatarExtId")
    val avatarExtId: String,
    @SerializedName("avatarUrl")
    val avatarUrl: String,
    @SerializedName("expiresIn")
    val expiresIn: Int,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("code")
    val code: String
)