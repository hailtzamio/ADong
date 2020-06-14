package com.zamio.adong.ui.driver

import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.item_header_layout.*

class MainDriverActivity : BaseActivity() {

    var isGoToChooseDriver = false
    override fun getLayout(): Int {
       return R.layout.activity_main_driver
    }

    override fun initView() {
        tvTitle.text = "Lái Xe"

    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            isGoToChooseDriver = true
        }
    }

    override fun resumeData() {

    }

}
