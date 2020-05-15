package com.zamio.adong.ui.project.tab.ui.main.checkinout

import ChooseTeamLeaderAdapter
import ProjectAttendenceAdapter
import RestClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.AttendanceCheckout
import com.zamio.adong.model.Worker
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_checkout_in_worker_list.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutInWorkerListActivity : BaseActivity() {

    var productId = 0
    override fun getLayout(): Int {
        return R.layout.activity_checkout_in_worker_list
    }

    override fun initView() {
        tvTitle.text = "Lịch Sử Điểm Danh"
        rightButton.visibility = View.GONE
    }

    override fun initData() {
        productId = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)
        if(productId != 0) {
            getData()
        }
    }

    override fun resumeData() {

    }

    private fun getData() {
        showProgessDialog()
        RestClient().getInstance().getRestService().getProjectAttendances(productId).enqueue(object :
            Callback<RestData<List<AttendanceCheckout>>> {
            override fun onFailure(call: Call<RestData<List<AttendanceCheckout>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<List<AttendanceCheckout>>>?,
                response: Response<RestData<List<AttendanceCheckout>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body().status == 1) {
                    setupRecyclerView(response.body().data!!)
                }
            }
        })
    }

    private fun setupRecyclerView(data: List<AttendanceCheckout>) {
        val mAdapter = ProjectAttendenceAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { worker ->

        }
    }

}
