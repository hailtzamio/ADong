package com.zamio.adong.ui.ware.stock

import RestClient
import android.content.Intent
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.project.ChooseManagerActivity
import kotlinx.android.synthetic.main.activity_create_stock.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateStockActivity : BaseActivity() {


    var type = "STOCK"
    override fun getLayout(): Int {
      return  R.layout.activity_create_stock
    }

    override fun initView() {
        tvTitle.text = "Tạo Kho"
        rightButton.visibility = View.GONE
    }

    override fun initData() {
        tvOk.setOnClickListener {

            if(isEmpty(edtBrand) || isEmpty(edtModel)){
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            val product = JsonObject()
            product.addProperty("name",edtBrand.text.toString())
            product.addProperty("address",edtModel.text.toString())
            product.addProperty("type",type)
            product.addProperty("keeperId",keeperId)
            create(product)
        }

        rlKeeper.setOnClickListener {
            val intent = Intent(this, ChooseManagerActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, 5)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun resumeData() {

    }

    private fun create(dataOb: JsonObject){
        showProgessDialog()
        RestClient().getInstance().getRestService().createWareHouse(dataOb).enqueue(object :
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

    var keeperId = 0
    var name = ""
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 5) {
            keeperId = data!!.getIntExtra("id", 0)
            name = data.getStringExtra("name")!!
            tvKeeper.text = name
        }
    }

}
