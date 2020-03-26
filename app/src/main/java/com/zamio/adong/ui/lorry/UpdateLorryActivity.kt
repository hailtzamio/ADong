package com.zamio.adong.ui.lorry

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
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_update_lorry.*

import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateLorryActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_update_lorry
    }

    override fun initView() {
        rightButton.visibility = View.GONE
        tvTitle.text = "Cập Nhật"

    }

    override fun initData() {
        val lorryOb = intent.extras!!.get(ConstantsApp.KEY_QUESTION_ID) as Lorry
        Log.e("hailpt","lorry  " + lorryOb.address)

            edtBrand.setText(lorryOb.brand)
            edtModel.setText(lorryOb.model)
            edtPlateNumber.setText(lorryOb.plateNumber)
            edtCapacity.setText(lorryOb.capacity)


        tvOk.setOnClickListener {

            if(isEmpty(edtBrand) || isEmpty(edtModel) || isEmpty(edtPlateNumber) || isEmpty(edtCapacity)){
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            val lorry = JsonObject()
            lorry.addProperty("brand",edtBrand.text.toString())
            lorry.addProperty("model",edtModel.text.toString())
            lorry.addProperty("plateNumber",edtPlateNumber.text.toString())
            lorry.addProperty("capacity",edtCapacity.text.toString())
            updateLorry(lorryOb.id,lorry )
        }
    }

    override fun resumeData() {

    }

    private fun updateLorry(id:Int, lorry: JsonObject){

        showProgessDialog()
        RestClient().getRestService().updateLorry(id,lorry).enqueue(object :
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
}
