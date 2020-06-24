package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Contractor(
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("districtId")
    val districtId: Int,
    @SerializedName("districtName")
    var districtName: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("contractorId")
    val contractorId: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("contractorName")
    var contractorName: String?,
    @SerializedName("contractorPhone")
    var contractorPhone: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("projectName")
    val projectName: String?,
    @SerializedName("provinceId")
    val provinceId: Int,
    @SerializedName("provinceName")
    var provinceName: String?,
    @SerializedName("rating")
    val rating: Float?,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String,
    @SerializedName("workingStatus")
    val workingStatus: String?,
    @SerializedName("isRegistered")
    val isRegistered: Boolean?
) : Serializable