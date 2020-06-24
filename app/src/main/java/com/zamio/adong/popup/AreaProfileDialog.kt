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
import kotlinx.android.synthetic.main.area_manager_profile_popup_layout.*


class AreaProfileDialog(context: Context, var data: AreaManager) :
    AlertDialog(context) {

    var onItemClick: ((AreaManager) -> Unit)? = null
    var mData = data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window!!.attributes.windowAnimations = R.style.DialogAnimationRightLeft
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        setContentView(R.layout.area_manager_profile_popup_layout)
        setCancelable(true)

        edt1.setText(mData.name ?: "")
        edt2.setText(mData.phone ?: "")
        edt3.setText(mData.email ?: "")

        btnOk.setOnClickListener {

            if(edt1.text.toString().trim() == "" || edt2.text.toString().trim() == "") {
                Toast.makeText(context, "Nhập thiếu thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mData.name = edt1.text.toString()
            mData.phone = edt2.text.toString()
            mData.email = edt3.text.toString()

            onItemClick?.invoke(mData)
            dismiss()
        }

    }


}





