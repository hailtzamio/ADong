package com.zamio.adong.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Information(
    var title: String,
    var name: String,
    var type: String
)

data class AreaManager (
    var name:String?,
    var phone:String?,
    var email:String?,
    var type : Int = 0
    ): Serializable