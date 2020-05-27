package com.zamio.adong.ui.team

import MemberTeamAdapter
import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.Team
import com.zamio.adong.model.Worker2
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_detail_team.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailTeamActivity : BaseActivity() {

    var teamId = 1
    var team:Team? = null
    var mAdapter:MemberTeamAdapter? = null
    override fun getLayout(): Int {
        return R.layout.activity_detail_team
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);

        if(!ConstantsApp.PERMISSION.contains("u")){
            rightButton.visibility = View.GONE
        }

        if(!ConstantsApp.PERMISSION.contains("d")){
            tvOk.visibility = View.GONE
        }
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)){

            teamId = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)

            tvOk.setOnClickListener {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                removeLorry()
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage("Chắc chắn sẽ xóa ?").setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show()
            }

            rightButton.setOnClickListener {
                val intent = Intent(this, UpdateTeamActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, team!!)
                startActivity(intent)
                this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    override fun resumeData() {
        getLorry(teamId)
        getWorkerFromTeamId(teamId)
    }

    private fun getLorry(id:Int){
        RestClient().getInstance().getRestService().getTeam(id).enqueue(object :
            Callback<RestData<Team>> {

            override fun onFailure(call: Call<RestData<Team>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<Team>>?, response: Response<RestData<Team>>?) {
                if( response!!.body() != null && response!!.body().status == 1){
                    team = response.body().data ?: return
                    if(team != null) {
                        tvName.text = team!!.name
                        tvLeaderName.text = team!!.leaderFullName
                        tvChooseWorker.text = "Danh sách công nhân" + " ( " +  team!!.teamSize.toString() + " )"
                        tvPhone.text = team!!.phone

                        if(team!!.phone2 != null) {
                            tvPhone2.text = team!!.phone2
                        } else {
                            tvPhone2.text = "---"
                        }


                        var address = ""

                        if(team!!.address != null) {
                            address =  team!!.address.toString()
                        }

                        if(team!!.districtName != null) {
                            address = address + " - " + team!!.districtName
                        }

                        if(team!!.provinceName != null) {
                            address = address + " - " + team!!.provinceName
                        }

                        tvAddress.text = address

                        if (team!!.workingStatus == "idle") {
                            tvStatus.text = "Đang rảnh"
                        } else {
                            tvStatus.text = "Đang bận"
                        }

                        if( team!!.rating != null) {
                            rating.rating = team!!.rating!!
                        }
                    }
                }
            }
        })
    }

    private fun getWorkerFromTeamId(teamId:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getWorkerFromTeam(teamId).enqueue(object :
            Callback<RestData<ArrayList<Worker2>>> {

            override fun onFailure(call: Call<RestData<ArrayList<Worker2>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<Worker2>>>?, response: Response<RestData<ArrayList<Worker2>>>?) {
                dismisProgressDialog()
                if( response!!.body() != null && response!!.body().status == 1){
                    setupRecyclerView(response.body().data!!)
                }
            }
        })
    }

    private fun setupRecyclerView(data:ArrayList<Worker2>){
        mAdapter = MemberTeamAdapter(data,false)
        recyclerViewTeamLeader.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
        recyclerViewTeamLeader.setHasFixedSize(false)
        recyclerViewTeamLeader.isNestedScrollingEnabled = true
        recyclerViewTeamLeader.adapter = mAdapter
        mAdapter!!.onItemClick = { position ->

        }
    }

    private fun removeLorry(){
        showProgessDialog()
        RestClient().getRestService().removeTeam(teamId).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if( response!!.body().status == 1){
                    showToast("Xóa thành công")
                    onBackPressed()
                }
            }
        })
    }

}
