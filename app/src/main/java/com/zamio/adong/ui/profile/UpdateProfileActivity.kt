package com.zamio.adong.ui.profile

import RestClient
import android.view.View
import android.widget.Toast
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.utils.PreferUtils
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.R
import com.zamio.adong.model.Lorry
import com.zamio.adong.model.Profile
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.utils.ProgressDialogUtils
import kotlinx.android.synthetic.main.activity_change_information.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProfileActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_change_information
    }

    override fun initView() {

        tvTitle.text = "Đổi Thông Tin"
        rightButton.visibility = View.GONE
        imvBack.setOnClickListener {
            onBackPressed()
        }

        tvOk.setOnClickListener {


            if (edt1.text.toString() == "" || edt2.text.toString() == "" ){
                Toast.makeText(this, "Nhập thiếu thông tin",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            changePassword()

        }


    }

    override fun initData() {
        val dataOb = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as Profile
        edt1.setText(dataOb.fullName ?: "")
        edt2.setText(dataOb.phone ?: "")
        edt3.setText(dataOb.email ?: "")
    }

    override fun resumeData() {

    }

    fun changePassword(){

        val json = JsonObject()
        json.addProperty("fullName",edt1.text.toString())
        json.addProperty("email",edt3.text.toString())
        json.addProperty("phone",edt2.text.toString())

        ProgressDialogUtils.showProgressDialog(this, 0, 0)
        RestClient().getInstance().getRestService().updateMyProfile(json).enqueue(object :
            Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                ProgressDialogUtils.dismissProgressDialog()
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                ProgressDialogUtils.dismissProgressDialog()
                if(response?.body() != null){
                    Toast.makeText(this@UpdateProfileActivity, "Thành công",Toast.LENGTH_SHORT).show()
                    onBackPressed()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }
}
