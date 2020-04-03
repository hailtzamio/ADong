package com.zamio.adong.ui.team

import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.Team
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_detail_team.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailTeamActivity : BaseActivity() {

    var teamId = 1
    var team:Team? = null
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
        if (intent.hasExtra(ConstantsApp.KEY_QUESTION_ID)){

            teamId = intent.getIntExtra(ConstantsApp.KEY_QUESTION_ID, 1)

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
//                val intent = Intent(this, UpdateLorryActivity::class.java)
//                intent.putExtra(ConstantsApp.KEY_QUESTION_ID, team!!)
//                startActivity(intent)
//                this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    override fun resumeData() {
        getLorry(teamId)
    }

    private fun getLorry(id:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getTeam(id).enqueue(object :
            Callback<RestData<Team>> {

            override fun onFailure(call: Call<RestData<Team>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<Team>>?, response: Response<RestData<Team>>?) {
                dismisProgressDialog()
                if( response!!.body().status == 1){
                    team = response.body().data ?: return
                    if(team != null) {
                        tvName.text = team!!.name
                        tvLeaderName.text = team!!.leaderFullName
                        tvSize.text = team!!.teamSize.toString()
                        tvPhone.text = team!!.phone
                        tvPhone2.text = team!!.phone2
                        tvAddress.text = team!!.address
                    }
                }
            }
        })
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
