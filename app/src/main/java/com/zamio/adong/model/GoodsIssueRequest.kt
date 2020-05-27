package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class GoodsIssueRequest(
    @SerializedName("linesAddNew")
    val linesAddNew: List<LinesAddNew>,
    @SerializedName("note")
    val note: String,
    @SerializedName("plannedDatetime")
    val plannedDatetime: String,
    @SerializedName("productReqId")
    val productReqId: Int,
    @SerializedName("warehouseId")
    val warehouseId: Int,
    @SerializedName("code")
    val code: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("warehouseName")
    val warehouseName: String,
    @SerializedName("productReqCode")
    val productReqCode: String
) {
    data class LinesAddNew(
        @SerializedName("productReqLineId")
        val productReqLineId: Int
    )
}