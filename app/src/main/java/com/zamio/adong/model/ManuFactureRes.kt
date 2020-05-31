package com.zamio.adong.model

data class ManuFactureRes(
    var code: String?,
    var createdByFullName: String?,
    var createdById: Int?,
    var createdTime: String?,
    var id: Int?,
    var note: String?,
    var plannedDatetime: String?,
    var productRequirementCode: String?,
    var productRequirementId: Int?,
    var projectAddress: String?,
    var projectId: Int?,
    var projectName: String?,
    var status: Int?,
    var updatedByFullName: String?,
    var updatedById: Int?,
    var updatedTime: String?,
    var warehouseId: Int?,
    var warehouseName: String?,
    var lines: ArrayList<MenuFactureResLine>?
)

data class MenuFactureResLine(
    var createdByFullName: String?,
    var createdById: Int?,
    var createdTime: String?,
    var id: Int?,
    var manufactureReqCode: String?,
    var manufactureReqId: Int?,
    var productId: Int?,
    var productName: String?,
    var productRequirementLineId: Int?,
    var productRequirementLineProductName: String?,
    var productUnit: String?,
    var quantity: Int?,
    var updatedByFullName: String?,
    var updatedById: Int?,
    var updatedTime: String?
)