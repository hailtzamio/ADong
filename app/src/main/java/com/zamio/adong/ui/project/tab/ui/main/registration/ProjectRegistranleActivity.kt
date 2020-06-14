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
import com.zamio.adong.ui.project.DetailProjectActivity
import kotlinx.android.synthetic.main.activity_choose_team_leader.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProjectRegistranleActivity : BaseActivity() {

    // Dành cho nhà thầu phụ đăng ký thi công công trình

    override fun getLayout(): Int {
       return R.layout.activity_choose_team_leader
    }

    override fun initView() {
        tvTitle.text = "Công Trình"
        rightButton.visibility = View.GONE

    }

    override fun initData() {
        getData()
    }

    override fun resumeData() {

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
                if( response!!.body().status == 1){
                    setupRecyclerView(response.body().data!!)

                    if(response.body().data!!.size == 0) {
                        showToast("Danh sách trống")
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
               val intent = Intent(this, DetailProjectActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, it.id)
            intent.putExtra(ConstantsApp.KEY_VALUES_HIDE, it.id)
            startActivity(intent)


        }
    }

}
