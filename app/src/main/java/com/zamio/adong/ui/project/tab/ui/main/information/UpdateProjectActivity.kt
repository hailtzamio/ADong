package com.zamio.adong.ui.project.tab.ui.main.information

import MemberTeamAdapter
import RestClient
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.R
import com.zamio.adong.model.AreaManager
import com.zamio.adong.model.Project
import com.zamio.adong.model.Province
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.popup.AreaProfileDialog
import com.zamio.adong.ui.map.MapActivity
import com.zamio.adong.ui.project.ChooseContractorActivity
import com.zamio.adong.ui.project.ChooseManagerActivity
import com.zamio.adong.ui.project.ChooseTeamActivity
import com.zamio.adong.utils.Utils
import kotlinx.android.synthetic.main.activity_create_project.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UpdateProjectActivity: BaseActivity() {

    var mAdapter: MemberTeamAdapter? = null
    var managerId = 0
    var deputyManagerId = 0
    var secretaryId = 0
    var teamId = 1
    var contractorId = 1
    var supervisorId = 0
    var provinces = ArrayList<Province>()
    var plannedStartDate = ""
    var plannedEndDate = ""
    var teamType = "ADONG"
    var id = 0
    var isChooseADong = true
    var latitude = 0.0
    var longitude = 0.0

    var investorManager = AreaManager("","","")
    var investorDeputyManager = AreaManager("","","")

    var dialog: AreaProfileDialog? = null
    var dialog2: AreaProfileDialog? = null

    override fun getLayout(): Int {
        return R.layout.activity_create_project
    }

    override fun initView() {
        tvTitle.text = "Sửa Công Trình"
        rightButton.visibility = View.GONE

        dialog = AreaProfileDialog(this, investorManager)
        dialog2 = AreaProfileDialog(this, investorDeputyManager)

        tvChooseDate.setOnClickListener {
            hideKeyboard()
            showDateTimePicker(true)
        }

        tvChooseEndDate.setOnClickListener {
            showDateTimePicker(false)
        }

        tvChooseLocation.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_LAT, latitude)
            intent.putExtra(ConstantsApp.KEY_VALUES_LONG, longitude)
            startActivityForResult(intent,1000)
        }

        rdGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = group.findViewById<View>(checkedId) as RadioButton
            val isChecked = checkedRadioButton.isChecked
            if (isChecked) {
                tvContractor.text = checkedRadioButton.text.toString()
                tvChooseTeamOrContractor.text = "Chọn"
            }

            teamType = if(checkedRadioButton.text.toString() == "Đội Á đông") {
                "ADONG"
            } else {
                "CONTRACTOR"
            }

            if (checkedRadioButton.text.toString() == "Đội Á đông") {
                isChooseADong = true
                tvContractor.text = "Tên đội *"
                tvChooseLeader.text = "Trưởng bộ phận *"
                tvDeputyName.text = "Phó bộ phận *"
                tvSeName.text = "Thư ký *"

            } else {
                isChooseADong = false
                tvContractor.text = "Tên đội"
                tvChooseLeader.text = "Trưởng bộ phận"
                tvDeputyName.text = "Phó bộ phận"
                tvSeName.text = "Thư ký *"
            }
        })
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
                    edtName.setText(data!!.name ?: "Chọn")
                    edtAddress.setText(data!!.address ?: "Chọn")
                    tvChooseDate.text = data!!.plannedStartDate ?: "2020-06-T12:12:12"
                    tvChooseEndDate.text = data!!.plannedEndDate ?: "2020-06-T12:12:12"
                    tvManagerName.text = data!!.managerFullName ?: "Chọn"

                    if(data!!.investorContacts != null) {
                        investorManager = data!!.investorContacts!!.manager
                        investorDeputyManager = data!!.investorContacts!!.deputyManager
                        tvManagerName.text = data!!.investorContacts!!.manager.name ?: "---"
                        tvDeputyManagerName.text = data!!.investorContacts!!.deputyManager.name ?: "---"

                        dialog = AreaProfileDialog(this@UpdateProjectActivity, investorManager)
                        dialog2 = AreaProfileDialog(this@UpdateProjectActivity, investorDeputyManager)
                    }

                    if(data!!.supervisorFullName != null ) {
                        tvLeaderName.text = data!!.supervisorFullName ?: "Chọn"
                    }

                    tvSecretaryName.text = data!!.secretaryFullName ?: "Chọn"

                    if (data!!.teamType == "ADONG") {
                        isChooseADong = true
                        rdGroup.check(R.id.rdAdong)
                    } else {
                        isChooseADong = false
                        rdGroup.check(R.id.rdContractor)
                    }

                    if (data!!.teamType == "ADONG") {
                        tvChooseTeamOrContractor.text = data!!.teamName ?: "Chọn"
                    } else {
                        tvChooseTeamOrContractor.text = data!!.contractorName ?: "Chọn"
                    }

                    plannedStartDate = tvChooseDate.text.toString()
                    plannedEndDate = tvChooseEndDate.text.toString()



                    if(data!!.secretaryId != null) {
                        secretaryId = data!!.secretaryId!!
                    }

                    if(data!!.supervisorId != null) {
                        supervisorId = data!!.supervisorId!!
                    }

                    if(data!!.contractorId != null) {
                        contractorId = data!!.contractorId!!
                    }

                    if(data!!.managerId != null) {
                        managerId = data!!.managerId!!
                    }

                    if(data!!.deputyManagerId != null) {
                        deputyManagerId = data!!.deputyManagerId!!
                    }

                    if(data!!.teamId != null) {
                        teamId = data!!.teamId!!
                    }

                    if(data!!.teamType != null) {
                        teamType = data!!.teamType!!
                    }

                    if(data!!.longitude != null) {
                        longitude = data!!.longitude!!
                    }

                    if(data!!.latitude != null) {
                        latitude = data!!.latitude!!
                    }
                }
            }
        })
    }

    private lateinit var date: Calendar
    private fun showDateTimePicker(isStartDate: Boolean) {
        val currentDate: Calendar = Calendar.getInstance()
        date = Calendar.getInstance()
        DatePickerDialog(
            this,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    this,
                    OnTimeSetListener { view, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        val format =
                            SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")

                        val formatToShow =
                            SimpleDateFormat("hh:mm a dd/MM/yyyy")

                        val dateTime = format.format(date.time).toString()
                        val dateTimeToShow = formatToShow.format(date.time).toString()
                        if (isStartDate) {
                            plannedStartDate = dateTime
                            tvChooseDate.text = dateTimeToShow
                        } else {
                            plannedEndDate = dateTime
                            tvChooseEndDate.text = dateTimeToShow
                        }
                    },
                    currentDate.get(Calendar.HOUR_OF_DAY),
                    currentDate.get(Calendar.MINUTE),
                    false
                ).show()
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DATE)
        ).show()
    }

    override fun initData() {

        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)
            getProject(id)
        }

        getProvinces()
        ConstantsApp.workers.clear()
        tvOk.setOnClickListener {

            if (isEmpty(edtName) || isEmpty(edtAddress)) {
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            if (isChooseADong) {
                if (checkChooseOrNot(tvChooseDate) || checkChooseOrNot(tvChooseEndDate) || checkChooseOrNot(
                        tvManagerName
                    )
                    || checkChooseOrNot(tvDeputyManagerName) || checkChooseOrNot(tvSecretaryName) || checkChooseOrNot(
                        tvContractor
                    ) || checkChooseOrNot(tvChooseTeamOrContractor)
                ) {
                    showToast("Chọn thiếu thông tin")
                    return@setOnClickListener
                }
            }

            if( checkChooseOrNot(tvSecretaryName)) {
                showToast("Chọn Thư ký")
                return@setOnClickListener
            }

            val product = JsonObject()
            product.addProperty("name", edtName.text.toString())
            product.addProperty("address", edtAddress.text.toString())
            product.addProperty("teamType", teamType)

            if (investorManager.name != "" && investorManager.phone != "") {
                product.addProperty("investorManagerName", investorManager.name)
                product.addProperty("investorManagerPhone", investorManager.phone)
                product.addProperty("investorManagerEmail", investorManager.email)
            } else {
                showToast("Chọn Trưởng bộ phận")
                return@setOnClickListener
            }

            if (investorDeputyManager.name != "" && investorDeputyManager.phone != "") {
                product.addProperty("investorDeputyManagerName", investorDeputyManager.name)
                product.addProperty("investorDeputyManagerPhone", investorDeputyManager.phone)
                product.addProperty("investorDeputyManagerEmail", investorDeputyManager.email)
            } else {
                showToast("Chọn Phó bộ phận")
                return@setOnClickListener
            }


            if (secretaryId != 0) {
                product.addProperty("secretaryId", secretaryId)
            }

            if (isChooseADong && teamId != 0) {
                product.addProperty("teamId", teamId)
            }
            if (!isChooseADong && contractorId != 0) {
                product.addProperty("contractorId", contractorId)
            }

            if (supervisorId != 0) {
                product.addProperty("supervisorId", supervisorId)
            }

            product.addProperty("plannedStartDate", plannedStartDate)
            product.addProperty("plannedEndDate", plannedEndDate)
            product.addProperty("latitude", latitude)
            product.addProperty("longitude", longitude)
            update(product)
        }

        tvManagerName.setOnClickListener {
//            val intent = Intent(this, ChooseManagerActivity::class.java)
//            intent.putExtra(ConstantsApp.KEY_VALUES_ID, 1)
//            startActivityForResult(intent, 1000)
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            if(dialog == null) {
                return@setOnClickListener
            }

            investorManager.type = 1

            dialog!!.show()
            dialog!!.onItemClick = {
                if (it.type == 1) {
                    investorManager = it
                    tvManagerName.text = it.name
                } else {
                    investorDeputyManager = it
                    tvDeputyManagerName.text = it.name
                }
            }
        }

        tvDeputyManagerName.setOnClickListener {
//            val intent = Intent(this, ChooseManagerActivity::class.java)
//            intent.putExtra(ConstantsApp.KEY_VALUES_ID, 2)
//            startActivityForResult(intent, 1000)
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            if(dialog2 == null) {
                return@setOnClickListener
            }

            investorDeputyManager.type = 2

            dialog2!!.show()
            dialog2!!.onItemClick = {
                if (it.type == 2)  {
                    investorDeputyManager = it
                    tvDeputyManagerName.text = it.name
                }
            }
        }

        tvLeaderName.setOnClickListener {
            val intent = Intent(this, ChooseManagerActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, 3)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        tvSecretaryName.setOnClickListener {
            val intent = Intent(this, ChooseManagerActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, 4)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        tvChooseTeamOrContractor.setOnClickListener {
            var intent: Intent? = null
            intent = if (isChooseADong) {
                Intent(this, ChooseTeamActivity::class.java)
            } else {
                Intent(this, ChooseContractorActivity::class.java)
            }

            intent.putExtra(ConstantsApp.KEY_VALUES_ID, 5)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }

    private fun checkChooseOrNot(tvTextView : TextView): Boolean {
        if(tvTextView.text == "Chọn") {
            return true
        }
        return false
    }

    override fun resumeData() {

    }

    private fun update(jsonObject: JsonObject) {
        showProgessDialog()
        RestClient().getInstance().getRestService().updateProjcet(id,jsonObject).enqueue(object :
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
                    showToast("Sửa thành công")
                    setResult(99)
                    finish()
                } else {
                    val obj = JSONObject(response.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }

    private fun getProvinces() {
        showProgessDialog()
        RestClient().getInstance().getRestService().getProvince().enqueue(object :
            Callback<RestData<ArrayList<Province>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Province>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<ArrayList<Province>>>?,
                response: Response<RestData<ArrayList<Province>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    provinces = response.body().data!!
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        val name = data.getStringExtra("name")
        val id = data.getIntExtra("id", 0)
        when (resultCode) {
            1 -> {
                tvManagerName.text = name
                managerId = id
            }
            2 -> {
                tvDeputyManagerName.text = name
                deputyManagerId = id
            }
            3 -> {
                tvLeaderName.text = name
                supervisorId = id
            }

            4 -> {
                tvSecretaryName.text = name
                secretaryId = id
            }

            5 -> {
                tvChooseLocation.text = name
                latitude = data.getDoubleExtra("latitude",0.0)
                longitude = data.getDoubleExtra("longitude",0.0)
            }

            100 -> tvChooseTeamOrContractor.text = name
        }

        if (resultCode == 100) {
            if (isChooseADong) {
                teamId = id
            } else {
                contractorId = id
            }
        }
    }
}
