package com.zamio.adong.popup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.zamio.adong.R
import kotlinx.android.synthetic.main.hold_sim_popup_layout.*


class CheckInOutDialog(context: Context) : AlertDialog(context) {

    var onItemClick: ((Int) -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window!!.attributes.windowAnimations = R.style.DialogAnimationRightLeft
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.checkin_out_popup_layout)
        setCancelable(true)



        btnClose.setOnClickListener {
            dismiss()
        }

        btnCancel.setOnClickListener {
            onItemClick?.invoke(1)
           dismiss()
        }

        btnOk.setOnClickListener {
            onItemClick?.invoke(2)
            dismiss()
        }
    }
}





