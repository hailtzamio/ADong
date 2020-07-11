package com.zamio.adong.ui.profile

import RestClient
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.utils.PreferUtils
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.zamio.adong.R
import com.zamio.adong.model.Profile
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.activity.LoginActivity
import com.zamio.adong.ui.lorry.UpdateLorryActivity
import com.zamio.adong.utils.ProgressDialogUtils
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.btnLogout
import kotlinx.android.synthetic.main.activity_profile.imvAva
import kotlinx.android.synthetic.main.activity_profile.lnChangePassword
import kotlinx.android.synthetic.main.activity_profile.lnUpdateProfile
import kotlinx.android.synthetic.main.activity_profile.tvEmail
import kotlinx.android.synthetic.main.activity_profile.tvName
import kotlinx.android.synthetic.main.activity_profile.tvPhone
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileActivity : BaseActivity() {


    var profile:Profile? = null
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

            val intent = Intent(this, UpdateProfileActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, profile!!)
            startActivity(intent)
             overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        lnChangePassword.setOnClickListener {
            startActivityForResult(Intent(this, ChangePasswordActivity::class.java), 1000)
        }

        imvAva.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(1, 1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)
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

                     profile = response.body()!!.data

                    if (profile != null) {
                        tvName.text = profile!!.fullName ?: "---"
                        tvEmail.text = profile!!.email ?: "---"
                        tvPhone.text = profile!!.phone ?: "---"

                        if(profile!!.avatarUrl != null && profile!!.avatarUrl != "") {
                            Picasso.get().load(profile!!.avatarUrl).error(R.drawable.ava).into(imvAva)
                        }

                    }

                }
            }
        })
    }

    fun updateProfile(){

        val json = JsonObject()
        json.addProperty("fullName",profile!!.fullName)
        json.addProperty("email",profile!!.email)
        json.addProperty("phone",profile!!.phone)
        json.addProperty("avatarExtId",profile!!.avatarExtId)

        ProgressDialogUtils.showProgressDialog(this, 0, 0)
        RestClient().getInstance().getRestService().updateMyProfile(json).enqueue(object :
            Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                ProgressDialogUtils.dismissProgressDialog()
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                ProgressDialogUtils.dismissProgressDialog()
                if(response?.body() != null) {
                    Toast.makeText(this@ProfileActivity, "Thành công", Toast.LENGTH_SHORT).show()
                    getProfile()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }

    private fun uploadImage(file: File) {
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body =
            MultipartBody.Part.createFormData("image", file.name, requestFile)

        showProgessDialog()
        RestClient().getRestService().updateProfile(body).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response?.body() != null && response.body().status == 1) {
//                    showToast("Tạo vật tư thành công " + response.body().data)
                    val idOb = response.body().data!!.asJsonObject
                    profile!!.avatarExtId = idOb["id"].asString
                    updateProfile()
                } else {
                    if (response!!.errorBody() != null) {
                        val obj = JSONObject(response.errorBody().string())
                        showToast(obj["message"].toString())
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri: Uri = result.uri
                val file = File(resultUri.path!!)
                uploadImage(file)
                imvAva.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}
