package com.zamio.adong.ui.project.tab.ui.main.registration

import ProjectAdapter
import RestClient
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Project
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.project.tab.ui.main.information.BasicInformationActivity
import kotlinx.android.synthetic.main.activity_choose_team_leader.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProjectRegistranleActivity : BaseActivity() {

    // Danh sách công trình để nhà thầu phụ đăng ký thi công


    var data = ArrayList<Project>()
    override fun getLayout(): Int {
       return R.layout.activity_choose_team_leader
    }

    override fun initView() {
        tvTitle.text = "Công Trình"
        rightButton.visibility = View.GONE

    }

    override fun initData() {

    }

    override fun resumeData() {
        data.clear()
        getData()
    }

    private fun getData(){
        showProgessDialog()
        RestClient().getInstance().getRestService().getProjectRegistrable().enqueue(object :
            Callback<RestData<ArrayList<Project>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Project>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<Project>>>?, response: Response<RestData<ArrayList<Project>>>?) {
                dismisProgressDialog()
                if( response!!.body() != null && response.body().status == 1){
                    data =  response.body().data ?: ArrayList<Project>()
                    setupRecyclerView(data)

                    if(response.body().data!!.size == 0) {
                        viewNoData.visibility = View.VISIBLE
                    } else {
                        viewNoData.visibility = View.GONE
                    }
                }
            }
        })
    }



    private fun setupRecyclerView(data:ArrayList<Project>){

        data.forEach {
            it.name = it.projectName ?: "---"
            it.address = it.projectAddress ?: "---"
        }

        val mAdapter = ProjectAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { it ->
               val intent = Intent(this, BasicInformationActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, it.id)
            intent.putExtra(ConstantsApp.KEY_VALUES_NEW_PROJECT, it.id)
            startActivity(intent)


        }
    }

}
