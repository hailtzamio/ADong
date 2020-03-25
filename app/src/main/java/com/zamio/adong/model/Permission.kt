package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class Permission(
    @SerializedName("action")
    val action: String,
    @SerializedName("appEntityCode")
    val appEntityCode: String,
    @SerializedName("appEntityId")
    val appEntityId: Int,
    @SerializedName("authorityId")
    val authorityId: Int,
    @SerializedName("id")
    val id: Int
)