package com.zamio.adong.ui.product

import RestClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.R
import com.zamio.adong.model.Lorry
import com.zamio.adong.model.Product
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_create_product.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProductActivity : BaseActivity() {
    override fun getLayout(): Int {
       return R.layout.activity_update_product
    }

    override fun initView() {
        rightButton.visibility = View.GONE
        tvTitle.text = "Cập Nhật"
    }

    override fun initData() {
        val productOb = intent.extras!!.get(ConstantsApp.KEY_QUESTION_ID) as Product


        edtName.setText(productOb.name)
        edtUnit.setText(productOb.unit)
        edtType.setText(productOb.type)

        tvOk.setOnClickListener {

            if(isEmpty(edtName) || isEmpty(edtUnit) || isEmpty(edtType)){
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            val product = JsonObject()
            product.addProperty("name",edtName.text.toString())
            product.addProperty("unit",edtUnit.text.toString())
            product.addProperty("type",edtType.text.toString())
            updateProduct(productOb.id,product )
        }
    }

    private fun updateProduct(id:Int, lorry: JsonObject){

        showProgessDialog()
        RestClient().getRestService().updateProduct(id,lorry).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if( response!!.body().status == 1){
                    showToast("Cập nhật thành công")
                    onBackPressed()
                }
            }
        })
    }

    override fun resumeData() {

    }



}
