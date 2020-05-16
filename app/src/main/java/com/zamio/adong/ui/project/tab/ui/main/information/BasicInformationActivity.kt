package com.zamio.adong.ui.project.tab.ui.main.information

import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.Project
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.project.tab.ProjectTabActivity
import com.zamio.adong.ui.project.tab.ui.main.checkinout.CheckoutInWorkerListActivity
import kotlinx.android.synthetic.main.activity_basic_information.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BasicInformationActivity : BaseActivity() {

    var id = 0
    override fun getLayout(): Int {
       return R.layout.activity_basic_information
    }

    override fun initView() {
        tvTitle.text = "Thông Tin"
        tvOk.visibility = View.GONE
        rightButton.setImageResource(R.drawable.icon_update);
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)
            getData(id)

            rightButton.setOnClickListener {
                val intent = Intent(this, UpdateProjectActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, id)
                startActivityForResult(intent, 1000)
            }
        }
    }

    override fun resumeData() {

    }

    fun removeProjectPopup() {
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (id != 0) {
                            removeProject()
                        }
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Xóa công trình?").setPositiveButton("Đồng ý", dialogClickListener)
            .setNegativeButton("Không", dialogClickListener).show()
    }

    var data: Project? = null
    fun getData(id: Int) {
        RestClient().getInstance().getRestService().getProject(id).enqueue(object :
            Callback<RestData<Project>> {

            override fun onFailure(call: Call<RestData<Project>>?, t: Throwable?) {

            }

            override fun onResponse(
                call: Call<RestData<Project>>?,
                response: Response<RestData<Project>>?
            ) {
                if (response!!.body() != null && response!!.body().status == 1 && tvName != null) {
                    data = response.body().data ?: return
                    tvName.text = data!!.name
                    tvAddress.text = data!!.address
                    tvChooseDate.text = data!!.plannedStartDate
                    tvChooseEndDate.text = data!!.plannedEndDate
                    tvManagerName.text = data!!.managerFullName
                    tvDeputyManagerName.text = data!!.deputyManagerFullName
                    tvLeaderName.text = data!!.supervisorFullName
                    tvSecretaryName.text = data!!.secretaryFullName
                    tvChooseTeamOrContractor.text = data!!.teamName
                    if (data!!.teamType == "ADONG") {
                        rlLeader.visibility = View.GONE
                        tvContractorOrTeam.text = "Đội Á đông"
                        rlLeader.visibility = View.GONE
                        tvLeaderNameAdong.text = data!!.teamLeaderFullName
//                        rlLeaderAdong.visibility = View.GONE // ??
                    } else {
                        rlLeaderAdong.visibility = View.GONE
                        rlLeader.visibility = View.VISIBLE
                        tvContractorOrTeam.text = data!!.contractorName
                        tvContractorOrTeamLabel.text = "Nhà thầu phụ"
                        lnTeamName.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun removeProject() {
        showProgessDialog()
        RestClient().getInstance().getRestService()
            .removeProject(id).enqueue(object :
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
                    } else {
                        val obj = JSONObject(response.errorBody().string())
                        Toast.makeText(this@BasicInformationActivity, obj["message"].toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

}
