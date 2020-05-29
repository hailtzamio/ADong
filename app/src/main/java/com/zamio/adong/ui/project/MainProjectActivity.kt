package com.zamio.adong.ui.project

import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.item_header_layout.*

class MainProjectActivity : BaseActivity() {

    var isChoooseProject = false
    override fun getLayout(): Int {
       return R.layout.activity_main_project
    }

    override fun initView() {
        tvTitle.text = "Công Trình"
    }

    override fun initData() {
        if(intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            isChoooseProject = true
        }
    }

    override fun resumeData() {

    }

    fun getIsChoooseProject() : Boolean {
        return isChoooseProject
    }
}
