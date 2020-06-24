package com.zamio.adong.popup

import DriverAdapter
import InformationAdapter
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.zamio.adong.R
import com.zamio.adong.model.Driver
import com.zamio.adong.model.Information
import com.zamio.adong.model.LinesAddNew
import kotlinx.android.synthetic.main.fragment_main_lorry_list.*
import kotlinx.android.synthetic.main.hold_sim_popup_layout.*


class ConfirmProductRequirementDialog(context: Context, data: ArrayList<LinesAddNew>) :
    AlertDialog(context) {

    var onItemClick: ((Int) -> Unit)? = null
    var mData = data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window!!.attributes.windowAnimations = R.style.DialogAnimationRightLeft
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.confirm_product_requirement_layout)
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


        val mList = ArrayList<Information>()

        mData.forEach {
            mList.add(Information(it.quantity.toString() + " " + it.unit, it.productName, ""))
        }

        val mAdapter = InformationAdapter(mList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

    }


}





