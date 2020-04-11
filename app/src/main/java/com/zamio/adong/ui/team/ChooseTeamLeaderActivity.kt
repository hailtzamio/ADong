package com.zamio.adong.ui.team

import ChooseTeamLeaderAdapter
import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Worker
import kotlinx.android.synthetic.main.activity_choose_team_leader.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChooseTeamLeaderActivity : BaseActivity() {
    
    override fun getLayout(): Int {
       return R.layout.activity_choose_team_leader
    }

    override fun initView() {
        tvTitle.text = "Danh Sách Đội Trưởng"
        rightButton.visibility = View.GONE

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

        mAdapter.onItemClick = { worker ->
            showPopupChooseLeader(worker)
        }
    }

    private fun showPopupChooseLeader(worker:Worker){
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val returnIntent = Intent()
                        returnIntent.putExtra("id", worker.id)
                        returnIntent.putExtra("avatarUrl", worker.avatarUrl)
                        returnIntent.putExtra("phone", worker.phone)
                        returnIntent.putExtra("name", worker.fullName)
                        setResult(100, returnIntent)
                        finish()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Chọn "+worker.fullName + " làm đội trưởng?").setPositiveButton("Đồng ý", dialogClickListener)
            .setNegativeButton("Không", dialogClickListener).show()
    }
}
