package com.zamio.adong.ui.profile

import RestClient
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.utils.PreferUtils
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.model.Profile
import com.zamio.adong.ui.activity.LoginActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.btnLogout
import kotlinx.android.synthetic.main.activity_profile.imvAva
import kotlinx.android.synthetic.main.activity_profile.lnChangePassword
import kotlinx.android.synthetic.main.activity_profile.lnUpdateProfile
import kotlinx.android.synthetic.main.activity_profile.tvEmail
import kotlinx.android.synthetic.main.activity_profile.tvName
import kotlinx.android.synthetic.main.activity_profile.tvPhone
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_profile
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun resumeData() {
        getProfile()

        btnLogout.setOnClickListener {
            val dialogClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {

                        DialogInterface.BUTTON_POSITIVE -> {
                            PreferUtils().setToken(this, "")
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()
                        }

                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Đăng xuất").setPositiveButton("Đồng ý", dialogClickListener)
                .setNegativeButton("Không", dialogClickListener).show()
        }

        lnUpdateProfile.setOnClickListener {
            //            startActivityForResult(Intent(context, UpdateProfileActivity::class.java),1000)
        }

        lnChangePassword.setOnClickListener {
            startActivityForResult(Intent(this, ChangePasswordActivity::class.java), 1000)
        }
    }

    private fun getProfile() {

        showProgessDialog()
        RestClient().getInstance().getRestService().getProfile().enqueue(object :
            Callback<RestData<Profile>> {
            override fun onFailure(call: Call<RestData<Profile>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<RestData<Profile>>?,
                response: Response<RestData<Profile>>?
            ) {
                dismisProgressDialog()
                if (response?.body() != null && response.body()!!.data != null) {

                    val profile = response.body()!!.data

                    if (profile != null) {
                        tvName.text = profile.fullName ?: "---"
                        tvEmail.text = profile.email ?: "---"
                        tvPhone.text = profile.phone ?: "---"

                        if(profile.avatarUrl != null && profile.avatarUrl != "") {
                            Picasso.get().load(profile.avatarUrl).error(R.drawable.ava).into(imvAva)
                        }

                    }

                }
            }
        })
    }
}
