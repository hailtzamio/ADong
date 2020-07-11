package com.zamio.adong.ui.criteria

import InformationAdapter
import SmallCriteriaAdapter
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import com.zamio.adong.model.Information
import com.zamio.adong.model.MarkSession
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.network.ConstantsApp.defaultDate
import com.zamio.adong.utils.Utils
import kotlinx.android.synthetic.main.activity_detail_criteria.*
import kotlinx.android.synthetic.main.activity_detail_criteria.recyclerView
import kotlinx.android.synthetic.main.activity_detail_driver.*
import kotlinx.android.synthetic.main.item_header_layout.*

class DetailCriteriaActivity : BaseActivity() {


    var data: MarkSession? = null
    var productId = 1
    val mList = ArrayList<Information>()
    override fun getLayout(): Int {
        return R.layout.activity_detail_criteria
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);
        rightButton.visibility = View.GONE
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {

            data = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as MarkSession

           rating.rating = data!!.point ?: 0.0f

            setupRecyclerView()

            mList.add(Information("Loại tiêu chí",data!!.criteriaBundleName ?: "---", ""))
            mList.add(Information("Công trình",data!!.projectName ?: "---", ""))
            mList.add(Information("Ngày đánh giá", Utils.convertDate(data!!.updatedTime ?: defaultDate), ""))
//            mList.add(Information("Người đánh giá",data!!.createdByFullName ?: "---", ""))
//            mList.add(Information("Người đánh giá",data!!.updatedByFullName ?: "---", ""))
            mList.add(Information("Ghi chú",data!!.note ?: "---", ""))

            setupRecyclerView2(mList)
        }


    }

    override fun resumeData() {

    }

    private fun setupRecyclerView2(data: List<Information>) {
        val mAdapter = InformationAdapter(data)
        recyclerView2.layoutManager = LinearLayoutManager(this)
        recyclerView2.setHasFixedSize(false)
        recyclerView2.adapter = mAdapter
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
