package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class GoodsNoteUpdateRq(val name: String) {
    var deliveredBy:String = ""
    var warehouseId:Int = 0
    var note:String = ""
    var ref:String = ""
    var linesAddNew =  ArrayList<LinesAddNew>()
    var linesDelete =  ArrayList<Int>()
    var linesUpdate =  ArrayList<LinesAddNew>()
}

data class Line(
    @SerializedName("id")
    val id: Int,
    @SerializedName("productId")
    val productId: Int,
    @SerializedName("quantity")
    val quantity: Int
)
