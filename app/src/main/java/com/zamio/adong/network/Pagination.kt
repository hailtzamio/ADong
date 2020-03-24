package com.zamio.adong.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Pagination {

    @SerializedName("totalRecords")
    @Expose
    var totalRecords: Int? = null

    @SerializedName("page")
    @Expose
    var page: Int? = null

    @SerializedName("size")
    @Expose
    var size: Int? = null

}