package com.zamio.adong.ui.project

import RestClient
import android.content.Intent
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.Project
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.lorry.map.LorryLocationActivity
import kotlinx.android.synthetic.main.activity_create_project.tvChooseDate
import kotlinx.android.synthetic.main.activity_create_project.tvChooseEndDate
import kotlinx.android.synthetic.main.activity_create_project.tvChooseTeamOrContractor
import kotlinx.android.synthetic.main.activity_create_project.tvDeputyManagerName
import kotlinx.android.synthetic.main.activity_create_project.tvLeaderName
import kotlinx.android.synthetic.main.activity_create_project.tvManagerName
import kotlinx.android.synthetic.main.activity_create_project.tvOk
import kotlinx.android.synthetic.main.activity_create_project.tvSecretaryName
import kotlinx.android.synthetic.main.activity_detail_project.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProjectActivity : BaseActivity() {

    var id = 0
    override fun getLayout(): Int {
        return R.layout.activity_detail_project
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.visibility = View.GONE
    }

    override fun initData() {

        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)
            getProject(id)
        }

        tvOk.setOnClickListener {
            if (id != 0) {
                removeProject()
            }
        }

        imvMap.setOnClickListener {
            val intent = Intent(this, LorryLocationActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_LAT, data!!.latitude)
            intent.putExtra(ConstantsApp.KEY_VALUES_LONG, data!!.longitude)
            startActivity(intent)
        }
    }

    var data: Project? = null
    private fun getProject(id: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getProject(id).enqueue(object :
            Callback<RestData<Project>> {

            override fun onFailure(call: Call<RestData<Project>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<Project>>?,
                response: Response<RestData<Project>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    data = response.body().data ?: return
                    tvName.text = data!!.name
                    tvAddress.text = data!!.address
                    tvChooseDate.text = data!!.plannedStartDate
                    tvChooseEndDate.text = data!!.plannedEndDate
                    tvManagerName.text = data!!.managerFullName
                    tvDeputyManagerName.text = data!!.deputyManagerFullName
                    tvLeaderName.text = data!!.supervisorFullName
                    tvSecretaryName.text = data!!.secretaryFullName
                    tvChooseTeamOrContractor.text = data!!.deputyManagerFullName
                    if (data!!.teamType == "ADONG") {
                        rlLeader.visibility = View.GONE
                        tvContractorOrTeam.text = "Đội Á đông"
                    } else {
                        rlLeader.visibility = View.VISIBLE
                        tvContractorOrTeam.text = data!!.contractorName
                        tvContractorOrTeamLabel.text = "Nhà thầu phụ"
                    }
                }
            }
        })
    }

    override fun resumeData() {

    }

    private fun removeProject() {
        showProgessDialog()
        RestClient().getInstance().getRestService().removeProject(id).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    showToast("Xóa thành công")
                    setResult(100)
                    finish()
                }
            }
        })
    }
}
