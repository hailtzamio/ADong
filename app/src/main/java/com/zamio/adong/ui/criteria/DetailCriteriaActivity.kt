package com.zamio.adong.ui.criteria

import SmallCriteriaAdapter
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import com.zamio.adong.model.MarkSession
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_detail_criteria.*
import kotlinx.android.synthetic.main.item_header_layout.*

class DetailCriteriaActivity : BaseActivity() {


    var data: MarkSession? = null
    var productId = 1
    override fun getLayout(): Int {
        return R.layout.activity_detail_criteria
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {

            data = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as MarkSession

            setupRecyclerView()
        }


    }

    override fun resumeData() {

    }

    private fun setupRecyclerView() {

        val mAdapter = SmallCriteriaAdapter(data!!.details!!)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            //            val intent = Intent(this, DetailCriteriaActivity::class.java)
//            intent.putExtra(ConstantsApp.KEY_QUESTION_ID, product.id)
//            startActivityForResult(intent,1000)
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 100) {

        }
    }

}
