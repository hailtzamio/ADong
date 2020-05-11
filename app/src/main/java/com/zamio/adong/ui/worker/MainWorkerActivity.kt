package com.zamio.adong.ui.worker

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.item_header_layout.*

class MainWorkerActivity : FragmentActivity() {

    var id = 0
    fun getLayout(): Int {
        return R.layout.activity_main_worker
    }

    fun initView() {
        tvTitle.text = "Công Nhân"
        rightButton.visibility = View.GONE
    }

    fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 0)
        }
    }

    fun resumeData() {

    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
//        initView()
        initData()
    }

    public fun getProjectId(): Int {
        return id
    }

    override fun onBackPressed() {
       setResult(102)
       finish()
    }

}
