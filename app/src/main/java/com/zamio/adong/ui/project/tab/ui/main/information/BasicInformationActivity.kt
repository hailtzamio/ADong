package com.zamio.adong.ui.project.tab.ui.main.information

import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.ui.network.UserRoles
import com.elcom.com.quizupapp.utils.PreferUtils
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.R
import com.zamio.adong.model.Contractor
import com.zamio.adong.model.Project
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.popup.AreaProfileDetailDialog
import com.zamio.adong.ui.lorry.map.LorryLocationActivity
import com.zamio.adong.ui.project.tab.ProjectTabActivity
import com.zamio.adong.utils.Utils
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

        if (!ConstantsApp.PERMISSION.contains("u")) {
            rightButton.visibility = View.GONE
        }

        if (!ConstantsApp.USER_ROLES.contains(UserRoles.Secretary.type)) {
            tvPause.visibility = View.GONE
        }

        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)

            getData(id)

            rightButton.setOnClickListener {
                val intent = Intent(this, UpdateProjectActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, id)
                startActivityForResult(intent, 1000)
            }

            imvMap.setOnClickListener {
                val intent = Intent(this, LorryLocationActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_LAT, data!!.latitude)
                intent.putExtra(ConstantsApp.KEY_VALUES_LONG, data!!.longitude)
                startActivity(intent)
            }

            if (intent.hasExtra(ConstantsApp.KEY_VALUES_NEW_PROJECT)) {
                // Đăng ký thi công  "NEW_PROJECT"
                tvPause.visibility = View.GONE
                rightButton.visibility = View.GONE
                tvOk.visibility = View.VISIBLE
                tvOk.text = "ĐĂNG KÝ THI CÔNG"
                checkHideShowRegistrationButton(id)
                tvOk.setOnClickListener {
                    showPopupRegisterProject()
                }
            } else {
                tvOk.setOnClickListener {
                    if (id != 0) {
                        removeProject()
                    }
                }
            }

            tvPause.setOnClickListener {


                var title = ""
                title = if (tvPause.text == "PHỤC HỒI") {
                    "Phục hồi công trình?"
                } else {
                    "Tạm dừng công trình?"
                }

                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                if (tvPause.text == "PHỤC HỒI") {
                                    doResumeProjectApi()
                                } else {
                                    doPauseProjectApi()
                                }
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage(title).setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show()
            }

//            lnAreaManager.setOnClickListener {
//
//                if(data != null && data!!.investorContacts != null) {
//                    val dialog = AreaProfileDetailDialog(this,data!!.investorContacts!!.manager)
//                    dialog.show()
//                }
//            }

            lnManager.setOnClickListener {

                if(data != null && data!!.investorContacts != null) {
                    val dialog = AreaProfileDetailDialog(this,data!!.investorContacts!!.manager)
                    dialog.show()
                }
            }

            lnDeputyManager.setOnClickListener {

                if(data != null && data!!.investorContacts != null) {
                    val dialog = AreaProfileDetailDialog(this,data!!.investorContacts!!.deputyManager)
                    dialog.show()
                }
            }

        }

        if (intent.hasExtra(ConstantsApp.KEY_VALUES_REG_APPROVED)) {
            // for  "REG_APPROVED"
            rightButton.visibility = View.GONE
            tvOk.visibility = View.GONE
            val registrationId = intent.getIntExtra(ConstantsApp.KEY_VALUES_REG_APPROVED, 0)
            getRegistationDetail(registrationId)
        }
    }

    private fun doPauseProjectApi() {

        val reason = JsonObject()
        reason.addProperty("note", "Need to Pause")

        showProgessDialog()
        RestClient().getRestService().pauseProject(id, reason).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    showToast("Thành công")
                    getData(id)
                } else {
                    if (response.errorBody() != null) {
                        val obj = JSONObject(response.errorBody().string())
                        showToast(obj["message"].toString())
                    }
                }
            }
        })
    }

    private fun doResumeProjectApi() {

        if (data == null) {
            return
        }

        val reason = JsonObject()
        reason.addProperty("teamType", data!!.teamType)
        reason.addProperty("teamId", data!!.teamId)
        if (data!!.contractorId != null) {
            reason.addProperty("contractorId", data!!.contractorId)
        } else {
            reason.addProperty("teamId", data!!.teamId)
        }

        reason.addProperty("note", "Need to Pause")

        showProgessDialog()
        RestClient().getRestService().pauseResume(id, reason).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    showToast("Thành công")
                    getData(id)
                } else {
                    if (response.errorBody() != null) {
                        val obj = JSONObject(response.errorBody().string())
                        showToast(obj["message"].toString())
                    }
                }
            }
        })
    }

    private fun getRegistationDetail(registrationId: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getRegistration(registrationId).enqueue(object :
            Callback<RestData<Project>> {
            override fun onFailure(call: Call<RestData<Project>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<Project>>?,
                response: Response<RestData<Project>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    if (response.body().data == null) return
                    id = response.body().data!!.projectId
                    getData(id)
                } else {
                    showToast("Không lấy được dữ liệu")
                }
            }
        })
    }

    private fun registerProject(id: Int) {

        val data = JsonObject()
        data.addProperty("note", "Đăng ký thi công")

        showProgessDialog()
        RestClient().getInstance().getRestService().registerProject(id, data).enqueue(object :
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
                    showToast("Thành công")
                    onBackPressed()
                } else {
                    val obj = JSONObject(response.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }

    private fun checkHideShowRegistrationButton(projectId: Int) {

        val preferUtils = PreferUtils()
        val userId = preferUtils.getUserId(this) ?: 0

        showProgessDialog()
        RestClient().getInstance().getRestService()
            .checkHideShowRegistrationButton(userId, projectId).enqueue(object :
                Callback<RestData<Contractor>> {
                override fun onFailure(call: Call<RestData<Contractor>>?, t: Throwable?) {
                    dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<Contractor>>?,
                    response: Response<RestData<Contractor>>?
                ) {
                    dismisProgressDialog()
                    if (response!!.body() != null && response.body().status == 1) {
                        if (response.body().data == null) return
                        if (response.body().data!!.isRegistered == true) {
                            tvOk.visibility = View.GONE
                        } else {
                            tvOk.visibility = View.VISIBLE
                        }


                    } else {
//                    showToast("Không lấy được dữ liệu")
                    }
                }
            })
    }

    private fun showPopupRegisterProject() {
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (data != null) {
                            registerProject(data!!.id)
                        }
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Đăng ký thi công?").setPositiveButton("Đồng ý", dialogClickListener)
            .setNegativeButton("Không", dialogClickListener).show()
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

                    if (data!!.plannedStartDate != null) {
                        tvChooseDate.text = Utils.convertDate(data!!.plannedStartDate)
                    }

                    if (data!!.plannedEndDate != null) {
                        tvChooseEndDate.text = Utils.convertDate(data!!.plannedEndDate)
                    }

                    if (data!!.investorContacts != null) {
                        tvManagerName.text = data!!.investorContacts!!.manager.name ?: "---"
                        tvDeputyManagerName.text =
                            data!!.investorContacts!!.deputyManager.name ?: "---"
                    }

                    tvLeaderName.text = data!!.supervisorFullName ?: "---"
                    tvSecretaryName.text = data!!.secretaryFullName ?: "---"
                    tvChooseTeamOrContractor.text = data!!.teamName ?: "---"
                    tvAreaManagerName.text = data!!.supervisorFullName ?: "---"

                    if (data!!.teamType == "ADONG") {
                        rlLeaderAdong.visibility = View.VISIBLE

                        rlLeader.visibility = View.GONE
                        tvContractorOrTeam.text = "Đội Á đông"
                        tvContractorOrTeamLabel.text = "Đội thi công"
                        rlLeader.visibility = View.GONE

                        if (data!!.teamLeaderFullName != null && data!!.teamLeaderFullName != "") {
                            tvLeaderNameAdong.text = data!!.teamLeaderFullName
                        }


//                        rlLeaderAdong.visibility = View.GONE // ??
                    } else {
                        rlLeaderAdong.visibility = View.GONE
                        rlLeader.visibility = View.VISIBLE
                        tvContractorOrTeam.text = data!!.contractorName ?: "---"
                        tvContractorOrTeamLabel.text = "Nhà thầu phụ"
                        lnTeamName.visibility = View.GONE
                    }


                    when (data!!.status ?: "NEW") {
                        "NEW" -> {
                            tvStatus.text = "Mới"
                        }
                        "PROCESSING" -> {
                            tvStatus.text = "Đang thi công"
                            tvPause.text = "TẠM DỪNG"
                        }
                        "DONE" -> {
                            rightButton.visibility = View.GONE
                            tvStatus.text = "Hoàn thành"
                            tvPause.visibility = View.GONE
                        }
                        "PAUSED" -> {
                            tvStatus.text = "Tạm dừng"
                            tvPause.text = "PHỤC HỒI"
                        }
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
                        Toast.makeText(
                            this@BasicInformationActivity,
                            obj["message"].toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getData(id)
    }

}
