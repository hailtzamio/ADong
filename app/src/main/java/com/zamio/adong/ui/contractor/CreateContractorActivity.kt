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
import com.zamio.adong.model.Province
import kotlinx.android.synthetic.main.activity_create_contractor.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateContractorActivity : BaseActivity() {

    val product = JsonObject()
    var provinces =ArrayList<Province>()
    var districts =ArrayList<Province>()
    var provinceId = 1
    var districtId = 1
    override fun getLayout(): Int {
        return R.layout.activity_create_contractor
    }

    override fun initView() {
        tvTitle.text = "Tạo Nhà Thầu Phụ"
        rightButton.visibility = View.GONE
    }

    override fun initData() {
        getProvinces()
        tvOk.setOnClickListener {

            if (isEmpty(edtName) || isEmpty(edtPhone) || isEmpty(edtPassword) || isEmpty(edtPwConfirm)) {
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

            if (edtPassword.text.toString() != edtPwConfirm.text.toString()) {
                showToast("Mật khẩu không trùng khớp")
                return@setOnClickListener
            }

            product.addProperty("name", edtName.text.toString())
            product.addProperty("password", edtPassword.text.toString())
            product.addProperty("address", edtAddress.text.toString())
            product.addProperty("email", edtEmail.text.toString())
            product.addProperty("phone", edtPhone.text.toString())
            product.addProperty("provinceId", provinceId)
            product.addProperty("districtId", districtId)
            product.addProperty("workingStatus", "idle")
            createProduct(product)
        }
    }

    fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    override fun resumeData() {

    }

    private fun createProduct(product: JsonObject) {
        showProgessDialog()
        RestClient().getInstance().getRestService().createContractor(product).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response?.body() != null && response.body().status == 1) {
                    showToast("Tạo thành công")
                    setResult(100)
                    finish()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }

    private fun getProvinces(){
        showProgessDialog()
        RestClient().getInstance().getRestService().getProvince().enqueue(object :
            Callback<RestData<ArrayList<Province>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Province>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<Province>>>?, response: Response<RestData<ArrayList<Province>>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response.body().status == 1){
                    provinces = response.body().data!!
                    setupProvinceSpinner()
                }
            }
        })
    }

    private fun getDistricts(provinceId:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getDistricts(provinceId).enqueue(object :
            Callback<RestData<ArrayList<Province>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Province>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<Province>>>?, response: Response<RestData<ArrayList<Province>>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response.body().status == 1){
                    districts = response.body().data!!
                    setupDistrictSpinner()
                }
            }
        })
    }

    private fun setupProvinceSpinner(){

        val mAdapter = CustomAdapter(this, R.layout.item_provice_spinner, R.id.tvName,provinces)

//        val dataAdapter = ArrayAdapter(
//            this,
//            R.layout.support_simple_spinner_dropdown_item, provinces
//        )
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
    }

    private fun setupDistrictSpinner() {

        val mAdapter = CustomAdapter(this, R.layout.item_provice_spinner, R.id.tvName,districts)
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
    }
}
