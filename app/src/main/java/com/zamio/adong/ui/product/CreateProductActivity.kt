package com.zamio.adong.ui.product

import RestClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.R
import com.zamio.adong.model.Product
import kotlinx.android.synthetic.main.activity_create_product.*
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateProductActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_create_product
    }

    override fun initView() {
        tvTitle.text = "Tạo Vật Tư"
    }

    override fun initData() {

        tvOk.setOnClickListener {

            if(isEmpty(edtName) || isEmpty(edtType) || isEmpty(edtUnit)){
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            val product = JsonObject()
            product.addProperty("name",edtName.text.toString())
            product.addProperty("type",edtType.text.toString())
            product.addProperty("unit",edtUnit.text.toString())
            createProduct(product)
        }

    }

    override fun resumeData() {

    }

    private fun createProduct(product:JsonObject){
        showProgessDialog()
        RestClient().getRestService().createUser(product).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if( response!!.body().status == 1){
                    showToast("Tạo vật tư thành công")
                    onBackPressed()
                }
            }
        })
    }


}
