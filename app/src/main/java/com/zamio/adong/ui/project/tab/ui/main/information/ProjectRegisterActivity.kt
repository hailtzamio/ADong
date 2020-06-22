package com.zamio.adong.ui.project.tab.ui.main.information

import ContructorAdapter
import RestClient
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Contractor
import com.zamio.adong.model.Project
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.contractor.DetailContractorActivity
import kotlinx.android.synthetic.main.activity_checkout_in_worker_list.*
import kotlinx.android.synthetic.main.activity_choose_team_leader.*
import kotlinx.android.synthetic.main.activity_choose_team_leader.recyclerView
import kotlinx.android.synthetic.main.activity_choose_team_leader.viewNoData
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProjectRegisterActivity : BaseActivity() {

    // Danh sách các nhà thầu phụ đăng ký thi công cho công trình này
    var data = ArrayList<Contractor>()
    var id = 0
    override fun getLayout(): Int {
       return R.layout.activity_choose_team_leader
    }

    override fun initView() {
        tvTitle.text = "Đăng Ký Thi Công"
        rightButton.visibility = View.GONE

    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)
            getData()
        }

    }

    override fun resumeData() {
        data.clear()
        getData()
    }

    private fun getData(){
        showProgessDialog()
        RestClient().getInstance().getRestService().getProjectRegister(id).enqueue(object :
            Callback<RestData<ArrayList<Contractor>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Contractor>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<Contractor>>>?, response: Response<RestData<ArrayList<Contractor>>>?) {
                dismisProgressDialog()
                if( response!!.body().status == 1){
                    data =  response.body().data ?: ArrayList<Contractor>()
                    setupRecyclerView(data)
                    if (response.body().data!!.isNotEmpty()) {
                        viewNoData.visibility = View.GONE
                    } else {
                        viewNoData.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun setupRecyclerView(data:ArrayList<Contractor>){

        data.forEach {
            it.name = it.contractorName ?: "---"

            it.phone = it.contractorPhone ?: "---"

            if(it.status == "NEW") {
                it.address =  "Mới"
            } else if(it.status == "APPROVED") {
                it.address =  "Đã duyệt"
            } else {
                it.address =  "Từ chối"
            }
        }

        val mAdapter = ContructorAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { it ->
               val intent = Intent(this, DetailContractorActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, it.contractorId)
            intent.putExtra(ConstantsApp.KEY_VALUES_STATUS, it.status)
            intent.putExtra(ConstantsApp.KEY_VALUES_HIDE, it.id)
            startActivity(intent)
        }
    }

}
