package com.zamio.adong.ui.contractor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zamio.adong.R
import kotlinx.android.synthetic.main.item_header_layout.*

class MainContractorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_contractor)
        tvTitle.text = "Nhà Thầu Phụ"
    }
}
