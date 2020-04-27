package com.zamio.adong.ui.criteria

import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import kotlinx.android.synthetic.main.item_header_layout.*

class MainCriteriaActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_main_criteria
    }

    override fun initView() {
      tvTitle.text = "Bộ Tiêu Chí"
    }

    override fun initData() {

    }

    override fun resumeData() {

    }
}
