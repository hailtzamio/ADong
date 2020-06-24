package com.zamio.adong.ui.project

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
import com.zamio.adong.model.Province
import com.zamio.adong.model.Worker
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.popup.AreaProfileDialog
import com.zamio.adong.popup.HoldSimDialog
import com.zamio.adong.ui.map.MapActivity
import com.zamio.adong.ui.trip.CreateTripActivity
import com.zamio.adong.ui.trip.TripActivity
import kotlinx.android.synthetic.main.activity_create_project.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CreateProjectActivity : BaseActivity() {

    var mAdapter: MemberTeamAdapter? = null
    var managerId = 0
    var deputyManagerId = 0
    var secretaryId = 0
    var leaderId = 0
    var teamId = 0
    var contractorId = 0
    var provinces = ArrayList<Province>()
    var plannedStartDate = ""
    var plannedEndDate = ""
    var teamType = "ADONG"
    var isChooseADong = true
    var latitude = 0.0
    var longitude = 0.0

    var investorManager = AreaManager("","","")
    var investorDeputyManager = AreaManager("","","")

    var dialog:AreaProfileDialog? = null
    var dialog2:AreaProfileDialog? = null

    override fun getLayout(): Int {
        return R.layout.activity_create_project
    }

    override fun initView() {
        tvTitle.text = "Tạo Công Trình"
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
            startActivityForResult(intent, 1000)
        }

        rdGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = group.findViewById<View>(checkedId) as RadioButton
            val isChecked = checkedRadioButton.isChecked
            if (isChecked) {
//                tvContractor.text = checkedRadioButton.text.toString()
                tvChooseTeamOrContractor.text = "Chọn"
            }

            teamType = if (checkedRadioButton.text.toString() == "Đội Á đông") {
                "ADONG"
            } else {
                "CONTRACTOR"
            }

            if (checkedRadioButton.text.toString() == "Đội Á đông") {
                isChooseADong = true
                rlLeader.visibility = View.GONE

                tvContractor.text = "Tên đội *"
                tvChooseLeader.text = "Trưởng bộ phận *"
                tvDeputyName.text = "Phó bộ phận *"
                tvSeName.text = "Thư ký *"

            } else {
                isChooseADong = false
                rlLeader.visibility = View.VISIBLE

                tvContractor.text = "Tên đội"
                tvChooseLeader.text = "Trưởng bộ phận"
                tvDeputyName.text = "Phó bộ phận"
                tvSeName.text = "Thư ký *"
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
        getProvinces()
        ConstantsApp.workers.clear()

        if (!checkPermissions()) {
            requestPermissions()
        }

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

            if (checkChooseOrNot(tvSecretaryName)) {
                showToast("Chọn thư ký")
                return@setOnClickListener
            }

//            if (!isChooseADong) {
//                if (checkChooseOrNot(tvLeaderName)) {
//                    showToast("Chọn thiếu thông tin")
//                    return@setOnClickListener
//                }
//            }

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

            if (teamId != 0) {
                product.addProperty("teamId", teamId)
            }
            if (contractorId != 0) {
                product.addProperty("contractorId", contractorId)
            }

            if (!isChooseADong && leaderId != 0) {
                product.addProperty("supervisorId", leaderId)
            }

            product.addProperty("plannedStartDate", plannedStartDate)
            product.addProperty("plannedEndDate", plannedEndDate)
            product.addProperty("latitude", latitude)
            product.addProperty("longitude", longitude)
            createProject(product)
        }

        tvManagerName.setOnClickListener {


//            val intent = Intent(this, ChooseManagerActivity::class.java)
//            intent.putExtra(ConstantsApp.KEY_VALUES_ID, 1)
//            startActivityForResult(intent, 1000)
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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

    private fun checkChooseOrNot(tvTextView: TextView): Boolean {
        if (tvTextView.text == "Chọn") {
            return true
        }
        return false
    }

    override fun resumeData() {

    }

    private fun createProject(jsonObject: JsonObject) {
        showProgessDialog()
        RestClient().getInstance().getRestService().createProject(jsonObject).enqueue(object :
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
                    showToast("Tạo thành công")
                    setResult(100)
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
                leaderId = id
            }

            4 -> {
                tvSecretaryName.text = name
                secretaryId = id
            }

            5 -> {
                tvChooseLocation.text = name
                latitude = data.getDoubleExtra("latitude", 0.0)
                longitude = data.getDoubleExtra("longitude", 0.0)
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
