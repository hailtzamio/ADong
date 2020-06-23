package com.zamio.adong.ui.trip

import RestClient
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.util.Log
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.Transport
import com.zamio.adong.model.TripRq
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.driver.MainDriverActivity
import com.zamio.adong.ui.lorry.MainLorryActivity
import kotlinx.android.synthetic.main.activity_create_trip.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateTripActivity : BaseActivity() {


    var transports = java.util.ArrayList<Transport>()
    var lorryId = 0
    var driverId = 0
    var plannedStartDate = ""
//    var tripRq = TripRq()
    override fun getLayout(): Int {
      return  R.layout.activity_create_trip
    }

    override fun initView() {
        tvTitle.text = "Tạo Xe"
        rightButton.visibility = View.GONE
    }

    override fun initData() {

        transports = ConstantsApp.transportsChoose
        for (i in transports.indices) {
            if(transports[i].isSelected == true) {
                Log.e("CreateTripActivity", "workersChoose  " + transports[i].code)
            }
        }

        tvOk.setOnClickListener {

            if(plannedStartDate == ""){
                showToast("Thiếu thông tin")
                return@setOnClickListener
            }

            if(lorryId == 0 || driverId == 0) {
                showToast("Thiếu thông tin")
                return@setOnClickListener
            }

            val transportReqIds = ArrayList<Int>()

            transports.forEach {
                if(it.isSelected == true) {
                    transportReqIds.add(it.id ?: 0)
                }
            }

            val tripRq = TripRq(plannedStartDate,driverId,lorryId,"",transportReqIds)

            createProduct(tripRq)
        }

        edt1.setOnClickListener {
            goToChooseLorry()
        }

        edt2.setOnClickListener {
            goToChooseDriver()
        }

        edt3.setOnClickListener {
            showDateTimePicker()
        }
    }

    private fun goToChooseLorry() {
        val intent = Intent(this, MainLorryActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID,1)
        startActivityForResult(intent, 1000)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun goToChooseDriver() {
        val intent = Intent(this, MainDriverActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID,2)
        startActivityForResult(intent, 1001)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun resumeData() {

    }

    private fun createProduct(tripRq: TripRq){
        showProgessDialog()
        RestClient().getInstance().getRestService().createTrip(tripRq).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if( response!!.body() != null && response!!.body().status == 1){
                    showToast("Thành công")
                    setResult(100)
                    finish()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }

    private lateinit var date: Calendar
    private fun showDateTimePicker() {
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
                        plannedStartDate = dateTime
                        edt3.text = dateTimeToShow
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(">>>>>>>>>>", "$resultCode")

        if(requestCode == 1000) {
            edt1.text = data!!.getStringExtra("name")
            lorryId = data.getIntExtra("id", 0)
        }

        if(requestCode == 1001) {
            edt2.text = data!!.getStringExtra("name")
            driverId = data.getIntExtra("id", 0)
        }
    }

}
