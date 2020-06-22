package com.zamio.adong.popup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import com.zamio.adong.R
import kotlinx.android.synthetic.main.activity_create_project.rdGroup
import kotlinx.android.synthetic.main.project_type_dialog.*


class ProjectTypeDialog(context: Context) : AlertDialog(context) {

    var onItemClick: ((String) -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window!!.attributes.windowAnimations = R.style.DialogAnimationRightLeft
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.project_type_dialog)
        setCancelable(true)

        rdGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = group.findViewById<View>(checkedId) as RadioButton
            val isChecked = checkedRadioButton.isChecked
            if (isChecked) {
//                tvContractor.text = checkedRadioButton.text.toString()

            }

            when(checkedRadioButton.text.toString()) {
                "Tất cả" ->  onItemClick?.invoke("")
                "Mới" ->  onItemClick?.invoke("NEW")
                "Đang thi công" ->  onItemClick?.invoke("PROCESSING")
                "Hoàn thành" ->  onItemClick?.invoke("DONE")
                "Tạm dừng" ->  onItemClick?.invoke("PAUSED")
            }

            Log.d("hailpt", checkedRadioButton.text.toString())

        })

        btnClose.setOnClickListener {
            dismiss()
        }

    }

}





