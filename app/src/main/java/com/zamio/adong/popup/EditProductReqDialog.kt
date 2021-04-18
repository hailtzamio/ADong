package com.zamio.adong.popup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.zamio.adong.R
import com.zamio.adong.model.Product
import kotlinx.android.synthetic.main.edit_product_req_layout.*


class EditProductReqDialog(context: Context, var data: Product) :
    AlertDialog(context) {

    var onItemClick: ((Product) -> Unit)? = null
    var mData = data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window!!.attributes.windowAnimations = R.style.DialogAnimationRightLeft
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        setContentView(R.layout.edit_product_req_layout)
        setCancelable(true)

        edt1.setText(mData.note ?: "")
        edt2.setText(mData.quantity.toString() ?: "")

        btnOk.setOnClickListener {

            if(edt1.text.toString().trim() == "" || edt2.text.toString().trim() == "") {
                Toast.makeText(context, "Nhập thiếu thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mData.note = edt1.text.toString()
            mData.quantity = edt2.text.toString().toDouble()

            onItemClick?.invoke(mData)
            dismiss()
        }

    }


}





