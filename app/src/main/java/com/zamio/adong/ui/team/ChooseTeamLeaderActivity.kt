package com.zamio.adong.ui.team

import ChooseTeamLeaderAdapter
import RestClient
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Worker
import kotlinx.android.synthetic.main.fragment_main_lorry_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseTeamLeaderActivity : BaseActivity() {
    override fun getLayout(): Int {
       return R.layout.activity_choose_team_leader
    }

    override fun initView() {

    }

    override fun initData() {
        getTeamLeader()
    }

    override fun resumeData() {

    }

    private fun getTeamLeader(){
        showProgessDialog()
        RestClient().getInstance().getRestService().getTeamLeaders(0,"").enqueue(object :
            Callback<RestData<List<Worker>>> {
            override fun onFailure(call: Call<RestData<List<Worker>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<List<Worker>>>?, response: Response<RestData<List<Worker>>>?) {
                dismisProgressDialog()
                if( response!!.body().status == 1){
                    setupRecyclerView(response.body().data!!)
                }
            }
        })
    }

    private fun setupRecyclerView(data:List<Worker>){
        val mAdapter = ChooseTeamLeaderAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
//            val intent = Intent(this, DetailLorryActivity::class.java)
//            intent.putExtra(ConstantsApp.KEY_QUESTION_ID, product.id)
//            startActivityForResult(intent,1000)
//           overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}
