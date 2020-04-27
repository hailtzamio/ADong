package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Team (


    @SerializedName("address")
    val address: String?,
    @SerializedName("addressFull")
    val addressFull: Any,
    @SerializedName("createdByFullName")
    val createdByFullName: String,
    @SerializedName("createdById")
    val createdById: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("districtId")
    val districtId: Int,
    @SerializedName("districtName")
    val districtName: String?,
    @SerializedName("futureProjectId")
    val futureProjectId: Any,
    @SerializedName("futureProjectName")
    val futureProjectName: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("leaderFullName")
    val leaderFullName: String,
    @SerializedName("leaderId")
    val leaderId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("phone2")
    val phone2: String,
    @SerializedName("projectId")
    val projectId: Any,
    @SerializedName("projectName")
    val projectName: Any,
    @SerializedName("provinceId")
    val provinceId: Int,
    @SerializedName("provinceName")
    val provinceName: String?,
    @SerializedName("rating")
    val rating: Float?,
    @SerializedName("teamSize")
    val teamSize: Int,
    @SerializedName("updatedByFullName")
    val updatedByFullName: String,
    @SerializedName("updatedById")
    val updatedById: Int,
    @SerializedName("updatedTime")
    val updatedTime: String,
    @SerializedName("workingStatus")
    val workingStatus: Any,
    @SerializedName("memberIds")
    val memberIds: List<Int>
) : Serializable