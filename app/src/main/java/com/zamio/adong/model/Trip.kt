package com.zamio.adong.model

data class Trip (
    var code: String?,
    var createdByFullName: String?,
    var createdById: Int?,
    var createdTime: String?,
    var driverFullName: String?,
    var driverId: Int?,
    var driverPhone: String?,
    var id: Int?,
    var lorryId: Int?,
    var lorryPlateNumber: String?,
    var name: String?,
    var note: String?,
    var plannedDatetime: String?,
    var status: Int?,
    var transportRequests: Int?,
    var updatedByFullName: String?,
    var updatedById: Int?,
    var updatedTime: String?
)