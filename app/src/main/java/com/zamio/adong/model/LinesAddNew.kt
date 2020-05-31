package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LinesAddNew(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("productReqLineId")
    var productReqLineId: Int,
    @SerializedName("productId")
    val productId: Int,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("productName")
    val productName: String
) : Serializable

data class IssueRequestLine(
    @SerializedName("productReqLineId")
    val productReqLineId: Int?,
    @SerializedName("productRequirementId")
    val productRequirementId: Int?,
    @SerializedName("productRequirementLineId")
    val productRequirementLineId: Int?
) : Serializable