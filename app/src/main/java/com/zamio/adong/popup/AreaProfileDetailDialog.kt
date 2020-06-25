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
import kotlinx.android.synthetic.main.area_manager_profile_detail_popup_layout.*


class AreaProfileDetailDialog(context: Context, var data: AreaManager) :
    AlertDialog(context) {

    var onItemClick: ((AreaManager) -> Unit)? = null
    var mData = data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window!!.attributes.windowAnimations = R.style.DialogAnimationRightLeft
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        setContentView(R.layout.area_manager_profile_detail_popup_layout)
        setCancelable(true)

        if(mData.email == "") {
            mData.email = null
        }

        tv1.text = mData.name ?: "---"
        tv2.text = mData.phone ?: "---"
        tv3.text = mData.email ?: "---"



        imv1.setOnClickListener {
            dismiss()
        }
    }


}





