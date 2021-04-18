package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class ProductRequirementRes(
    @SerializedName("expectedDatetime")
    val expectedDatetime: String,
    @SerializedName("linesAddNew")
    val linesAddNew: ArrayList<LinesAddNew>,
    @SerializedName("note")
    val note: String,
    @SerializedName("linesRemove")
    val linesRemove: ArrayList<LinesAddNew>? = null ,
    @SerializedName("linesUpdate")
    val linesUpdate: ArrayList<LinesAddNew>? = null
)
