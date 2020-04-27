package com.zamio.adong.ui.contractor

import RestClient
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.R
import com.zamio.adong.adapter.CustomAdapter
import com.zamio.adong.model.Contractor
import com.zamio.adong.model.Province
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_update_contractor.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateContractorActivity : BaseActivity() {


    var avatarUrl = ""
    var avatarExtId = ""
    val contractor = JsonObject()
    var isTeamLeader = false
    var provinceId = 1
    var districtId = 1
    var contractorId = 1
    var provinces = ArrayList<Province>()
    var districts = ArrayList<Province>()
    override fun getLayout(): Int {
       return R.layout.activity_update_contractor
    }

    override fun initView() {
        rightButton.visibility = View.GONE
        tvTitle.text = "Cập Nhật"
    }

    override fun initData() {

        val productOb = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as Contractor
        getProvinces()
        edtName.setText(productOb.name)
        edtPhone.setText(productOb.phone)
        edtAddress.setText(productOb.address)
        edtEmail.setText(productOb.email)

        provinceId = productOb.provinceId
        districtId = productOb.districtId
        contractorId = productOb.id

        tvOk.setOnClickListener {

            if (isEmpty(edtName) || isEmpty(edtPhone)) {
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            if (edtPhone.text.toString().length != 10) {
                showToast("Sai định dạng số điện thoại")
                return@setOnClickListener
            }

            if (edtEmail.text.toString().trim() != "" && !edtEmail.text.toString().isValidEmail()) {
                showToast("Sai định dạng email")
                return@setOnClickListener
            }

            contractor.addProperty("name", edtName.text.toString())
            contractor.addProperty("address", edtAddress.text.toString())
            contractor.addProperty("email", edtEmail.text.toString())
            contractor.addProperty("phone", edtPhone.text.toString())
            contractor.addProperty("provinceId", provinceId)
            contractor.addProperty("districtId", districtId)
            updateWorker(productOb.id, contractor )
        }
    }

    fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun updateWorker(id:Int, woker: JsonObject){

        showProgessDialog()
        RestClient().getInstance().getRestService().updateContractor(id,woker).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response!!.body().status == 1){
                    showToast("Cập nhật thành công")
                    setResult(100)
                    finish()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }

    override fun resumeData() {

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
                    setupProvinceSpinner()
                }
            }
        })
    }

    private fun getDistricts(provinceId: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getDistricts(provinceId).enqueue(object :
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
                    districts = response.body().data!!
                    setupDistrictSpinner()
                }
            }
        })
    }

    private fun setupProvinceSpinner() {
        var selection = 0
        for (i in 0 until provinces.size) {
            if (provinces[i].id == provinceId) {
                selection = i
            }
        }

        val mAdapter = CustomAdapter(this, R.layout.item_provice_spinner, R.id.tvName, provinces)
        mAdapter.setDropDownViewResource(R.layout.item_provice_spinner)
        spinProvince.adapter = mAdapter
        spinProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                provinceId = provinces[position].id
                getDistricts(provinceId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinProvince.setSelection(selection)
    }

    private fun setupDistrictSpinner() {

        var selection = 0
        for (i in 0 until districts.size) {
            if (districts[i].id == districtId) {
                selection = i
            }
        }

        val mAdapter = CustomAdapter(this, R.layout.item_provice_spinner, R.id.tvName, districts)
        mAdapter.setDropDownViewResource(R.layout.item_provice_spinner)
        spinDistrict.adapter = mAdapter
        spinDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                districtId = districts[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        spinDistrict.setSelection(selection)
    }


}
