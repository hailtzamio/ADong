package com.zamio.adong.ui.profile

import RestClient
import android.view.View
import android.widget.Toast
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.utils.PreferUtils
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.R
import com.zamio.adong.utils.ProgressDialogUtils
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_change_password
    }

    override fun initView() {

        tvTitle.text = "Đổi Mật Khẩu"
        rightButton.visibility = View.GONE
        imvBack.setOnClickListener {
            onBackPressed()
        }

        tvOk.setOnClickListener {

            val curentPw = PreferUtils().getPassword(this)

            if(curentPw != edtOldPw.text.toString()){
                Toast.makeText(this, "Mật khẩu hiện tại không đúng",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (edtPw.text.toString() == "" || edtOldPw.text.toString() == "" || edtPwConfirm.text.toString() == ""){
                Toast.makeText(this, "Nhập thiếu thông tin",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(edtPw.text.toString().length < 6) {
                Toast.makeText(this, "Mật khẩu ít nhất 6 ký tự",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (edtPw.text.toString() != edtPwConfirm.text.toString()){
                Toast.makeText(this, "Mật khẩu không trùng khớp",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            changePassword()

        }


    }

    override fun initData() {

    }

    override fun resumeData() {

    }

    fun changePassword(){

        val json = JsonObject()
        json.addProperty("currentPassword",edtOldPw.text.toString())
        json.addProperty("newPassword",edtPw.text.toString())

        ProgressDialogUtils.showProgressDialog(this, 0, 0)
        RestClient().getInstance().getRestService().changeMyPassword(json).enqueue(object :
            Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                ProgressDialogUtils.dismissProgressDialog()
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                ProgressDialogUtils.dismissProgressDialog()
                if(response?.body() != null){
                    Toast.makeText(this@ChangePasswordActivity, "Đã thay đổi mật khâu",Toast.LENGTH_SHORT).show()
                    PreferUtils().setPassword(this@ChangePasswordActivity, edtPw.text.toString())
                    onBackPressed()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }
}
