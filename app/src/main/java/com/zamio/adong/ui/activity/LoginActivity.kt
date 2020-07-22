package com.zamio.adong.ui.activity

import RestClient
import android.content.Intent
import android.os.Environment
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.utils.PreferUtils
import com.google.gson.JsonObject
import com.onesignal.OneSignal
import com.zamio.adong.MainActivity
import com.zamio.adong.R
import com.zamio.adong.model.User
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class LoginActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_login
    }

    override fun initView() {

    }

    override fun initData() {

        tvOk.setOnClickListener {

            if(isEmpty(edtUsername) || isEmpty(edtPassword)){
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            val user = JsonObject()
            user.addProperty("username",edtUsername.text.toString())
            user.addProperty("password",edtPassword.text.toString())

            login(user)
        }

    }

    fun writeFile(): String {

        val wallpaperDirectory = File(
            Environment.getDataDirectory().toString() + "/SimTD"
        )
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }

        val f = File(wallpaperDirectory, Calendar.getInstance()
            .timeInMillis.toString())

//        try {
//            val f = File(
//                wallpaperDirectory, Calendar.getInstance()
//                    .timeInMillis.toString() + ".jpg"
//            )
//            f.createNewFile()
//            val fo = FileOutputStream(f)
//            fo.write(bytes.toByteArray())
//            MediaScannerConnection.scanFile(
//                this,
//                arrayOf(f.path),
//                arrayOf("image/jpeg"), null
//            )
//
//            fo.close()
//            Log.d("TAG", "File Saved::--->" + f.absolutePath)
//
        return f.absolutePath
//        } catch (e1: IOException) {
//            e1.printStackTrace()
//        }

        return ""
    }

    override fun resumeData() {

    }

    private fun login(user: JsonObject){
        showProgessDialog()
        RestClient().getInstance().getRestService().login(user).enqueue(object :
            Callback<User> {

            override fun onFailure(call: Call<User>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                dismisProgressDialog()
                if( response?.body() != null &&  ( response.body().message == "Logged in successfully" ||  response.body().message == "Đăng nhập thành công")){
                    ConstantsApp.BASE64_AUTH_TOKEN = response.body().accessToken
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    val preferUtils = PreferUtils()
                    preferUtils.setToken(this@LoginActivity, ConstantsApp.BASE64_AUTH_TOKEN)
                    preferUtils.setPassword(this@LoginActivity,edtPassword.text.toString())
                    preferUtils.setUserId(this@LoginActivity,response.body().userId)
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    OneSignal.sendTag("user_id",response.body().userId.toString())

                } else {
                    showToast("Sai tên đăng nhập hoặc mật khẩu")
                }
            }
        })
    }
}
