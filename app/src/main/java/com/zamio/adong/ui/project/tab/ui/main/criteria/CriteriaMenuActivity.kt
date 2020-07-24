package com.zamio.adong.ui.project.tab.ui.main.criteria

import CriteriaMenuAdapter
import RestClient
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.CriteriaMenu
import com.zamio.adong.model.MarkSession
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.criteria.DetailCriteriaActivity
import kotlinx.android.synthetic.main.activity_criteria_menu.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CriteriaMenuActivity : BaseActivity() {

    var data = ArrayList<CriteriaMenu>()
    var markSessions = ArrayList<MarkSession>()
    var id = 0
    override fun getLayout(): Int {
        return R.layout.activity_criteria_menu
    }

    override fun initView() {
            rightButton.visibility = View.GONE
        tvTitle.text = "Đánh Giá Công Trình"
    }

    override fun initData() {

        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)

            getMarkSessions()
        }
    }

    override fun resumeData() {


    }

    private fun setupRecyclerView() {

        val mAdapter = CriteriaMenuAdapter(data)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            var isCanClick = false
            markSessions.forEach {
                if (it.criteriaBundleId == (product.value ?: "0").toInt()) {
                    val intent = Intent(this, DetailCriteriaActivity::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_ID, it)
                    startActivityForResult(intent, 1000)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    isCanClick = true
                }
            }

            if(!isCanClick) {
                showToast("Chưa có đánh giá")
            }
        }
    }

    private fun getData() {

        showProgessDialog()
        RestClient().getInstance().getRestService().getSysParams().enqueue(object :
            Callback<RestData<ArrayList<CriteriaMenu>>> {
            override fun onFailure(call: Call<RestData<ArrayList<CriteriaMenu>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<ArrayList<CriteriaMenu>>>?,
                response: Response<RestData<ArrayList<CriteriaMenu>>>?
            ) {
                dismisProgressDialog()
//                data.clear()
                if (response!!.body() != null && response!!.body().status == 1) {
                    val dataTem = response.body().data!!
                    dataTem.forEach {
                        if (it.value != null) {
                            data.add(it)
                        }
                    }


                    for (i in 1 until markSessions.size) {
                        for (j in 1 until data.size) {
                                if (markSessions[i].criteriaBundleId == (data[j].value ?: "0").toInt()) {
                                    data[j].score = markSessions[i].point
                                }
                        }
                    }


                    setupRecyclerView()
                }
            }
        })
    }

    private fun getMarkSessions() {

        showProgessDialog()
        RestClient().getInstance().getRestService().getMarkSessions(id).enqueue(object :
            Callback<RestData<ArrayList<MarkSession>>> {

            override fun onFailure(call: Call<RestData<ArrayList<MarkSession>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<MarkSession>>>?, response: Response<RestData<ArrayList<MarkSession>>>?
            ) {
                dismisProgressDialog()

                if (response!!.body() != null && response.body().status == 1) {
                    markSessions = response.body().data!!
                    getData()

                }
            }
        })
    }

}
