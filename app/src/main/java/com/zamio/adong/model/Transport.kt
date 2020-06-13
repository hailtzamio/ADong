package com.zamio.adong.model

data class Transport (
    var code: String?,
    var createdByFullName: String?,
    var createdById: Int?,
    var createdTime: String?,
    var id: Int?,
    var note: String?,
    var plannedDatetime: String?,
    var projectAddress: String?,
    var projectId: Int?,
    var projectName: String?,
    var ref: String?,
    var status: Int?,
    var tripId: Int?,
    var tripName: String?,
    var updatedByFullName: String?,
    var updatedById: Int?,
    var updatedTime: String?,
    var warehouseId: Int?,
    var warehouseName: String?,
    var lines : ArrayList<Product>?
)