package com.zamio.adong.ui.product

import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.item_header_layout.*


class MainProductActivity : BaseActivity() {

    override fun getLayout(): Int {
       return R.layout.activity_items_product
    }

    override fun initView() {
        tvTitle.text = "Vật Tư"
    }

    override fun initData() {

    }

    override fun resumeData() {

    }

}
