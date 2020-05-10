package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class LinesAddNew(
    @SerializedName("productId")
    val productId: Int,
    @SerializedName("quantity")
    val quantity: Int
)