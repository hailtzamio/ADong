package com.zamio.adong.ui.project.tab.ui.main.checkinout

import RestClient
import WorkOutlineAdapter
import WorkerAdapter
import WorkerCheckinOutAdapter
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.adapter.PaginationScrollListener
import com.zamio.adong.model.Worker
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_check_in_out.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckInOutActivity : BaseActivity() {

    var page = 0
    var data = ArrayList<Worker>()
    var id = 0
    override fun getLayout(): Int {
       return R.layout.activity_check_in_out
    }

    override fun initView() {

    }

    override fun initData() {


        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
             id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)
            setupRecyclerView()
            getData(page)
        }
    }

    override fun resumeData() {

    }

    fun getData(pPage:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getProjectWorkers(id,pPage).enqueue(object :
            Callback<RestData<List<Worker>>> {
            override fun onFailure(call: Call<RestData<List<Worker>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<List<Worker>>>?, response: Response<RestData<List<Worker>>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response.body().status == 1){
                    data.addAll(response.body().data!!)
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    val mAdapter = WorkerCheckinOutAdapter(data)
    private fun setupRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        if( recyclerView != null) {
            recyclerView.layoutManager = layoutManager
            recyclerView.setHasFixedSize(false)
            recyclerView.adapter = mAdapter

            mAdapter.onItemClick = { product ->

            }
        }
    }
}
