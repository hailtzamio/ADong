package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class CriteriaValues(
    @SerializedName("criteria")
    val criteria: List<CriteriaSmall>,
    @SerializedName("name")
    val name: String
)

data class CriteriaSmall(
    @SerializedName("id")
    val id: Int,
    @SerializedName("factor")
    var factor: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("status")
    var status: String = "old"
)

data class CriteriaSmallUpdate(
    @SerializedName("id")
    val id: Int,
    @SerializedName("factor")
    var factor: Int,
    @SerializedName("name")
    var name: String
)