package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class CheckinOut(
    @SerializedName("projectId")
    var projectId: Int?,
    @SerializedName("workerIds")
    var workerIds: ArrayList<Int>?,
    @SerializedName("checkinTime")
    var checkinTime: String?,
    @SerializedName("checkoutTime")
    var checkoutTime: String?
)