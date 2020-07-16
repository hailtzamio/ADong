package com.zamio.adong.popup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.zamio.adong.R
import com.zamio.adong.model.AreaManager
import com.zamio.adong.model.Project
import com.zamio.adong.utils.Utils
import kotlinx.android.synthetic.main.project_map_popup_layout.*


class ProjectMaplDialog(context: Context, var data: Project) :
    AlertDialog(context) {

    var onItemClick: ((Project) -> Unit)? = null
    var mData = data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window!!.attributes.windowAnimations = R.style.DialogAnimationRightLeft
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        setContentView(R.layout.project_map_popup_layout)
        setCancelable(true)

        tv0.text = mData.name ?: "Tên Công Trình"
        tv2.text = Utils.convertDate(mData.plannedStartDate ?: "2020-07-11T06:47:47" )
        tv4.text =Utils.convertDate(mData.plannedEndDate ?: "2020-07-11T06:47:47")
        if(mData.teamType ?: "ADONG" == "ADONG" ) {
            tv3.text = mData.teamName ?: "---"
        } else {
            tv3.text = mData.contractorName ?: "---"
        }

        imv1.setOnClickListener {
            dismiss()
        }

        bt1.setOnClickListener {
            onItemClick!!.invoke(mData)
        }
    }


}





