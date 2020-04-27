package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class UpdateCriteria(
    @SerializedName("addNew")
    val addNew: List<CriteriaSmall>,
    @SerializedName("name")
    val name: String,
    @SerializedName("remove")
    val remove: List<Int>,
    @SerializedName("update")
    val update: List<Update>
)

data class Update(
    @SerializedName("criteriaBundleId")
    val criteriaBundleId: Int,
    @SerializedName("factor")
    val factor: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)