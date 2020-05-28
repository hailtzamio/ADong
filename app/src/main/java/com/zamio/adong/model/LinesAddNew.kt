package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LinesAddNew(
    @SerializedName("id")
    val id: Int,
    @SerializedName("productId")
    val productId: Int,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("productName")
    val productName: String
) : Serializable