package com.zamio.adong.ui.trip

import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.item_header_layout.*

class TripActivity : BaseActivity() {

    var isGoToChoose = false
    override fun getLayout(): Int {
        return R.layout.activity_main_trip
    }

    override fun initView() {
        tvTitle.text = "Chuyến Đi"
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            isGoToChoose = true
        }
    }

    override fun resumeData() {

    }

    fun getIsGoToChoose() : Boolean {
        return isGoToChoose
    }
}
