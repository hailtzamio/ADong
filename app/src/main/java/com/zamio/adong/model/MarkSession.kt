package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MarkSession(
    @SerializedName("createdByFullName")
    var createdByFullName: String?,
    @SerializedName("createdById")
    var createdById: Int?,
    @SerializedName("createdTime")
    var createdTime: String?,
    @SerializedName("criteriaBundleId")
    var criteriaBundleId: Int?,
    @SerializedName("criteriaBundleName")
    var criteriaBundleName: String?,
    @SerializedName("details")
    var details: List<Detail>?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("note")
    var note: String?,
    @SerializedName("point")
    var point: Double?,
    @SerializedName("projectId")
    var projectId: Int?,
    @SerializedName("projectName")
    var projectName: String?,
    @SerializedName("type")
    var type: String?,
    @SerializedName("updatedByFullName")
    var updatedByFullName: String?,
    @SerializedName("updatedById")
    var updatedById: Int?,
    @SerializedName("updatedTime")
    var updatedTime: String?
) : Serializable

data class Detail(
    @SerializedName("createdByFullName")
    var createdByFullName: String?,
    @SerializedName("createdById")
    var createdById: Int?,
    @SerializedName("createdTime")
    var createdTime: String?,
    @SerializedName("criterionId")
    var criterionId: Int?,
    @SerializedName("criterionName")
    var criterionName: String?,
    @SerializedName("factor")
    var factor: Int?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("markSessionCriteriaBundleName")
    var markSessionCriteriaBundleName: String?,
    @SerializedName("markSessionId")
    var markSessionId: Int?,
    @SerializedName("point")
    var point: Float?,
    @SerializedName("updatedByFullName")
    var updatedByFullName: String?,
    @SerializedName("updatedById")
    var updatedById: Int?,
    @SerializedName("updatedTime")
    var updatedTime: String?
): Serializable