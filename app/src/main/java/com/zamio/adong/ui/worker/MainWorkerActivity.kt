package com.zamio.adong.ui.worker

import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import kotlinx.android.synthetic.main.item_header_layout.*

class MainWorkerActivity : BaseActivity() {

    override fun getLayout(): Int {
      return R.layout.activity_main_worker
    }

    override fun initView() {
        tvTitle.text = "Công Nhân"
    }

    override fun initData() {

    }

    override fun resumeData() {

    }

}
