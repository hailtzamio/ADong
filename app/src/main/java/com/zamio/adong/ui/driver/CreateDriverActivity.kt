package com.zamio.adong.ui.driver

import RestClient
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.zamio.adong.R
import kotlinx.android.synthetic.main.activity_create_driver.*
import kotlinx.android.synthetic.main.activity_create_worker.cropImageView
import kotlinx.android.synthetic.main.activity_create_worker.edtEmail
import kotlinx.android.synthetic.main.activity_create_worker.edtName
import kotlinx.android.synthetic.main.activity_create_worker.edtPhone
import kotlinx.android.synthetic.main.activity_create_worker.tvOk
import kotlinx.android.synthetic.main.item_header_layout.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class CreateDriverActivity : BaseActivity() {

    var thumbnailExtId = ""
    val product = JsonObject()
    var isTeamLeader = false
    override fun getLayout(): Int {
        return R.layout.activity_create_driver
    }

    override fun initView() {
        tvTitle.text = "Tạo Lái Xe"
        rightButton.visibility = View.GONE
    }

    override fun initData() {

        tvOk.setOnClickListener {

            if (thumbnailExtId == "") {
                showToast("Chọn ảnh")
                return@setOnClickListener
            }

            if (isEmpty(edtName) || isEmpty(edtPhone) || isEmpty(edtPassword) || isEmpty(edtPwConfirm)) {
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            if (edtPhone.text.toString().length != 10) {
                showToast("Sai định dạng số điện thoại")
                return@setOnClickListener
            }

            if (edtEmail.text.toString().trim() != "" && !edtEmail.text.toString().isValidEmail()) {
                showToast("Sai định dạng email")
                return@setOnClickListener
            }

            if (edtPassword.text.toString() != edtPwConfirm.text.toString()) {
                showToast("Mật khẩu không trùng khớp")
                return@setOnClickListener
            }

            product.addProperty("fullName", edtName.text.toString())
            product.addProperty("phone2", edtPhone2.text.toString())
            product.addProperty("phone", edtPhone.text.toString())
            product.addProperty("email", edtEmail.text.toString())
            product.addProperty("password", edtPassword.text.toString())
            product.addProperty("avatarExtId", thumbnailExtId)
            product.addProperty("workingStatus", "idle")
            createProduct(product)
        }

        cropImageView.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(1, 1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)
        }
    }

    private fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    override fun resumeData() {

    }

    private fun createProduct(product: JsonObject) {
        showProgessDialog()
        RestClient().getInstance().getRestService().createDriver(product).enqueue(object :
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
                    showToast("Tạo lái xe thành công")
                    setResult(100)
                    finish()
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
                    thumbnailExtId = idOb["id"].asString
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
                cropImageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}
