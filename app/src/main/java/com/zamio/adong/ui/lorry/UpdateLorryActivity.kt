package com.zamio.adong.ui.lorry

import RestClient
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
        val dataOb = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as Lorry
            edtBrand.setText(dataOb.brand)
            edtModel.setText(dataOb.model)
            edtPlateNumber.setText(dataOb.plateNumber)
            edtCapacity.setText(dataOb.capacity)

        tvOk.setOnClickListener {

            if(isEmpty(edtBrand) || isEmpty(edtModel) || isEmpty(edtPlateNumber) || isEmpty(edtCapacity)){
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            val data = JsonObject()
            data.addProperty("brand",edtBrand.text.toString())
            data.addProperty("model",edtModel.text.toString())
            data.addProperty("plateNumber",edtPlateNumber.text.toString())
            data.addProperty("capacity",edtCapacity.text.toString())
            update(dataOb.id,data )
        }
    }

    override fun resumeData() {

    }

    private fun update(id:Int, data: JsonObject){

        showProgessDialog()
        RestClient().getInstance().getRestService().updateLorry(id,data).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response!!.body().status == 1){
                    showToast("Cập nhật thành công")
                    onBackPressed()
                }
            }
        })
    }
}
