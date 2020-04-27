package com.zamio.adong.ui.project

import ContructorAdapter
import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Contractor
import kotlinx.android.synthetic.main.activity_choose_team_leader.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChooseContractorActivity : BaseActivity() {
    
    override fun getLayout(): Int {
       return R.layout.activity_choose_to_make_project
    }

    override fun initView() {
        tvTitle.text = "Danh Sách Thầu Phụ"
        rightButton.visibility = View.GONE

    }

    override fun initData() {
        getTeamLeader()
    }

    override fun resumeData() {

    }

    private fun getTeamLeader(){
        showProgessDialog()
        RestClient().getInstance().getRestService().getContractors(0,"").enqueue(object :
            Callback<RestData<List<Contractor>>> {
            override fun onFailure(call: Call<RestData<List<Contractor>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<List<Contractor>>>?, response: Response<RestData<List<Contractor>>>?) {
                dismisProgressDialog()
                if( response!!.body().status == 1){
                    setupRecyclerView(response.body().data!!)
                }
            }
        })
    }

    private fun setupRecyclerView(data:List<Contractor>){
        val mAdapter = ContructorAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { data ->
            showPopupChooseLeader(data)
        }
    }

    private fun showPopupChooseLeader(data:Contractor){
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val returnIntent = Intent()
                        returnIntent.putExtra("id", data.id)
                        returnIntent.putExtra("phone", data.phone)
                        returnIntent.putExtra("name", data.name)
                        setResult(100, returnIntent)
                        finish()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Chọn "+data.name +" ?").setPositiveButton("Đồng ý", dialogClickListener)
            .setNegativeButton("Không", dialogClickListener).show()
    }
}
