package com.zamio.adong.ui.lorry

import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.item_header_layout.*

class MainLorryActivity : BaseActivity() {

    var isGoToChooseLorry = false
    override fun getLayout(): Int {
        return R.layout.activity_main_lorry
    }

    override fun initView() {
        tvTitle.text = "Xe Hàng"
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            isGoToChooseLorry = true
        }
    }

    override fun resumeData() {

    }
}
