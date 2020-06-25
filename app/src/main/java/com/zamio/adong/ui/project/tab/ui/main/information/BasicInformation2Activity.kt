package com.zamio.adong.ui.project.tab.ui.main.information

import InformationAdapter
import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.ui.network.UserRoles
import com.elcom.com.quizupapp.utils.PreferUtils
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.R
import com.zamio.adong.model.Contractor
import com.zamio.adong.model.Information
import com.zamio.adong.model.Project
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.popup.AreaProfileDetailDialog
import com.zamio.adong.ui.lorry.map.LorryLocationActivity
import com.zamio.adong.ui.project.tab.ProjectTabActivity
import com.zamio.adong.utils.Utils
import kotlinx.android.synthetic.main.activity_basic_informationn.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BasicInformation2Activity : BaseActivity() {

    var id = 0
    var data: Project? = null
    val mList = ArrayList<Information>()
    override fun getLayout(): Int {
        return R.layout.activity_basic_informationn
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

                    }
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

    private fun setupRecyclerView(mList: List<Information>) {
        val mAdapter = InformationAdapter(mList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter
        mAdapter.onItemClick = {
            when (it) {
                1 -> {
                    if (data != null && data!!.investorContacts != null) {
                        val dialog =
                            AreaProfileDetailDialog(this, data!!.investorContacts!!.manager)
                        dialog.show()
                    }
                }

                2 -> {
                    if (data != null && data!!.investorContacts != null) {
                        val dialog =
                            AreaProfileDetailDialog(this, data!!.investorContacts!!.deputyManager)
                        dialog.show()
                    }
                }
            }

        }
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

        reason.addProperty("note", "Tạm dừng CT")

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

                    mList.clear()

                    tvName.text = data!!.name
                    tvAddress.text = data!!.address

                    if (data!!.plannedStartDate != null) {
                        tvChooseDate.text = Utils.convertDate(data!!.plannedStartDate)
                    }

                    if (data!!.plannedEndDate != null) {
                        tvChooseEndDate.text = Utils.convertDate(data!!.plannedEndDate)
                    }

                    if (data!!.teamType == "ADONG") {
                        mList.add(Information("Đội Á Đông", data!!.teamName ?: "---", ""))
                    } else {
                        mList.add(Information("Nhà thầu phụ", data!!.contractorName ?: "---", ""))
                    }

                    if (data!!.investorContacts != null) {
                        mList.add(
                            Information(
                                "Trưởng bộ phận",
                                data!!.investorContacts!!.manager.name ?: "---",
                                "Show"
                            )
                        )
                        mList.add(
                            Information(
                                "Phó bộ phận",
                                data!!.investorContacts!!.deputyManager.name ?: "---",
                                "Show"
                            )
                        )
                    }

                    mList.add(Information("Quản lý vùng", data!!.supervisorFullName ?: "---", ""))
                    mList.add(Information("Đội trưởng", data!!.teamLeaderFullName ?: "---", ""))
                    mList.add(Information("Thư ký", data!!.secretaryFullName ?: "---", ""))

                    setupRecyclerView(mList)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getData(id)
    }

}
