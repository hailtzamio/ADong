package com.zamio.adong.ui.lorry

import RestClient
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.R
import kotlinx.android.synthetic.main.activity_create_lorry.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateLorryActivity : BaseActivity() {

    override fun getLayout(): Int {
      return  R.layout.activity_create_lorry
    }

    override fun initView() {
        tvTitle.text = "Tạo Xe"
        rightButton.visibility = View.GONE
    }

    override fun initData() {
        tvOk.setOnClickListener {

            if(isEmpty(edtBrand) || isEmpty(edtModel) || isEmpty(edtPlateNumber) || isEmpty(edtCapacity)){
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            if(edtPlateNumber.text.length < 8 || edtPlateNumber.text.length > 12 ){
                showToast("Biển số xe sai định dạng")
                return@setOnClickListener
            }

            val product = JsonObject()
            product.addProperty("brand",edtBrand.text.toString())
            product.addProperty("model",edtModel.text.toString())
            product.addProperty("plateNumber",edtPlateNumber.text.toString())
            product.addProperty("capacity",edtCapacity.text.toString())
            createProduct(product)
        }
    }

    override fun resumeData() {

    }

    private fun createProduct(lorry: JsonObject){
        showProgessDialog()
        RestClient().getInstance().getRestService().createLorry(lorry).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if( response!!.body() != null && response!!.body().status == 1){
                    showToast("Tạo xe thành công")
                    setResult(100)
                    finish()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }

}
