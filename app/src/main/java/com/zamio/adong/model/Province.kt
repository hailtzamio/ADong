package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class Province(
    @SerializedName("code")
    val code: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)