package com.zamio.adong.ui.project.tab.ui.main.logs

import ProjectLogsAdapter
import RestClient
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.LogsProject
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_project_logs.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectLogsActivity : BaseActivity() {


    var id = 0
    override fun getLayout(): Int {
       return R.layout.activity_project_logs
    }

    override fun initView() {
        rightButton.visibility = View.GONE
        tvTitle.text = "Lịch Sử Thay Đổi"
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)
            getData()
        }

    }

    override fun resumeData() {

    }

    private fun getData() {
        showProgessDialog()
        RestClient().getInstance().getRestService().getProjectLogs(id).enqueue(object :
            Callback<RestData<ArrayList<LogsProject>>> {
            override fun onFailure(call: Call<RestData<ArrayList<LogsProject>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<ArrayList<LogsProject>>>?,
                response: Response<RestData<ArrayList<LogsProject>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    setupRecyclerView(response.body().data!!)
                }
            }
        })
    }

    private fun setupRecyclerView(data: ArrayList<LogsProject>) {
        val mAdapter = ProjectLogsAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->

        }
    }

}