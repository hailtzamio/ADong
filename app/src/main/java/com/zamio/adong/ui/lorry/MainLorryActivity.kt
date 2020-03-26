package com.zamio.adong.ui.lorry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp

import kotlinx.android.synthetic.main.item_header_layout.*

class MainLorryActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_main_lorry
    }

    override fun initView() {
        tvTitle.text = "Xe Hàng"
    }

    override fun initData() {

    }

    override fun resumeData() {

    }

}
