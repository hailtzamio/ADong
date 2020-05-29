package com.zamio.adong.model

data class GoodsIssueRequest(
    var code: String?,
    var createdByFullName: String?,
    var createdById: Int?,
    var createdTime: String?,
    var id: Int?,
    var lines: ArrayList<GoodsIssueRequestLine>?,
    var note: String?,
    var plannedDatetime: String?,
    var productReqCode: String?,
    var productReqId: Int?,
    var status: Int?,
    var updatedByFullName: String?,
    var updatedById: Int?,
    var updatedTime: String?,
    var warehouseId: Int?,
    var warehouseName: String?
)

data class GoodsIssueRequestLine(
    var createdByCreatedTime: String?,
    var createdById: Int?,
    var createdTime: String?,
    var id: Int?,
    var productCode: String?,
    var productId: Int?,
    var productName: String?,
    var productReqLineId: Int?,
    var productReqLineProductName: String?,
    var productUnit: String?,
    var quantity: Int?,
    var reqId: Int?,
    var status: Int?,
    var updatedByCreatedTime: String?,
    var updatedById: Int?,
    var updatedTime: String?
)