package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Project(
    @SerializedName("accountingStatus")
    val accountingStatus: Int?,
    @SerializedName("actualEndDate")
    val actualEndDate: String?,
    @SerializedName("actualStartDate")
    val actualStartDate: String?,
    @SerializedName("address")
    var address: String?,
    @SerializedName("contractorId")
    val contractorId: Int?,
    @SerializedName("contractorName")
    val contractorName: String?,
    @SerializedName("createdByFullName")
    val createdByFullName: String?,
    @SerializedName("createdById")
    val createdById: Int?,
    @SerializedName("createdTime")
    val createdTime: String?,
    @SerializedName("deputyManagerFullName")
    val deputyManagerFullName: String?,
    @SerializedName("deputyManagerId")
    val deputyManagerId: Int?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("managerFullName")
    val managerFullName: String?,
    @SerializedName("managerId")
    val managerId: Int?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("projectName")
    var projectName: String?,
    @SerializedName("projectAddress")
    val projectAddress: String?,
    @SerializedName("plannedEndDate")
    val plannedEndDate: String?,
    @SerializedName("plannedStartDate")
    val plannedStartDate: String?,
    @SerializedName("qualityCriteriaBundleId")
    val qualityCriteriaBundleId: Int,
    @SerializedName("qualityCriteriaBundleName")
    val qualityCriteriaBundleName: String,
    @SerializedName("safetyCriteriaBundleId")
    val safetyCriteriaBundleId: Int,
    @SerializedName("safetyCriteriaBundleName")
    val safetyCriteriaBundleName: String,
    @SerializedName("secretaryCriteriaBundleId")
    val secretaryCriteriaBundleId: Int,
    @SerializedName("secretaryCriteriaBundleName")
    val secretaryCriteriaBundleName: String?,
    @SerializedName("secretaryFullName")
    val secretaryFullName: String?,
    @SerializedName("secretaryId")
    val secretaryId: Int?,
    @SerializedName("status")
    val status: String,
    @SerializedName("supervisorFullName")
    val supervisorFullName: String?,
    @SerializedName("supervisorId")
    val supervisorId: Int,
    @SerializedName("supplyChainCriteriaBundleId")
    val supplyChainCriteriaBundleId: Int,
    @SerializedName("supplyChainCriteriaBundleName")
    val supplyChainCriteriaBundleName: String,
    @SerializedName("teamId")
    val teamId: Int?,
    @SerializedName("teamLeaderFullName")
    val teamLeaderFullName: String?,
    @SerializedName("teamLeaderId")
    val teamLeaderId: Int,
    @SerializedName("teamName")
    val teamName: String?,
    @SerializedName("teamType")
    val teamType: String?,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String
) : Serializable