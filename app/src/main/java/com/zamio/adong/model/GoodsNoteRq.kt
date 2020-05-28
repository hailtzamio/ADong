package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class GoodsNoteRq(
    @SerializedName("deliveredBy")
    val deliveredBy: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("ref")
    val ref: String,
    @SerializedName("warehouseId")
    val warehouseId: Int,
    @SerializedName("lines")
    val lines: ArrayList<LinesAddNew>
)