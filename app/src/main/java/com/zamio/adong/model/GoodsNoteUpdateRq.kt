package com.zamio.adong.model


import com.google.gson.annotations.SerializedName

data class GoodsNoteUpdateRq(val name: String) {

    var deliveredBy:String = ""
    var plannedDatetime:String = ""
    var receiver:String = ""
    var warehouseId:Int = 0
    var productReqId:Int = 0
    var projectId:Int = 0
    var note:String = ""
    var reason:String = ""
    var ref:String = ""
    var linesAddNew =  ArrayList<LinesAddNew>()
    var linesDelete =  ArrayList<Int>()
    var linesUpdate =  ArrayList<LinesAddNew>()
}

data class GoodsNoteUpdateRq2(val name: String) {

    var plannedDatetime:String = ""
    var warehouseId:Int = 0
    var assigneeId:Int = 0
    var productReqId:Int = 0
    var productRequirementId:Int = 0
    var note:String = ""
    var linesAddNew =  ArrayList<IssueRequestLine>()
}


