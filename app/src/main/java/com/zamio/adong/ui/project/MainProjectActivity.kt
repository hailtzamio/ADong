package com.zamio.adong.ui.project

import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import kotlinx.android.synthetic.main.item_header_layout.*

class MainProjectActivity : BaseActivity() {
    override fun getLayout(): Int {
       return R.layout.activity_main_project
    }

    override fun initView() {
        tvTitle.text = "Công Trình"
    }

    override fun initData() {

    }

    override fun resumeData() {

    }
}
