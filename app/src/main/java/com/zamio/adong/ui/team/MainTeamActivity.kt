package com.zamio.adong.ui.team

import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import kotlinx.android.synthetic.main.item_header_layout.*

class MainTeamActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_main_team
    }

    override fun initView() {
        tvTitle.text = "Đội Á Đông"
    }

    override fun initData() {

    }

    override fun resumeData() {

    }

}
