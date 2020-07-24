package com.zamio.adong.ui.ware.stock.stock

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
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.zamio.adong.R
import com.zamio.adong.model.Driver
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_update_driver.*
import kotlinx.android.synthetic.main.activity_update_worker.cropImageView
import kotlinx.android.synthetic.main.activity_update_worker.edtEmail
import kotlinx.android.synthetic.main.activity_update_worker.edtName
import kotlinx.android.synthetic.main.activity_update_worker.edtPhone
import kotlinx.android.synthetic.main.activity_update_worker.tvOk
import kotlinx.android.synthetic.main.item_header_layout.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UpdateStockActivity : BaseActivity() {


    var avatarUrl = ""
    var avatarExtId = ""
    val worker = JsonObject()
    var isTeamLeader = false
    override fun getLayout(): Int {
       return R.layout.activity_update_driver
    }

    override fun initView() {
        rightButton.visibility = View.GONE
        tvTitle.text = "Cập Nhật"
    }

    override fun initData() {
        val model = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as Driver

        avatarExtId = model.avatarExtId
        edtName.setText(model.fullName)
        edtPhone.setText(model.phone)
        edtPhone2.setText(model.phone2)
        edtEmail.setText(model.email)
        Picasso.get().load(model.avatarUrl).into(cropImageView)
        tvOk.setOnClickListener {

            if(isEmpty(edtName) || isEmpty(edtPhone)){
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            if (edtPhone.text.toString().length != 10) {
                showToast("Sai định dạng số điện thoại")
                return@setOnClickListener
            }

            if (edtEmail.text.toString().isNotEmpty() &&!edtEmail.text.toString().isValidEmail()) {
                showToast("Sai định dạng email")
                return@setOnClickListener
            }

            worker.addProperty("fullName",edtName.text.toString())
            worker.addProperty("phone",edtPhone.text.toString())
            worker.addProperty("phone2",edtPhone2.text.toString())
            worker.addProperty("email",edtEmail.text.toString())
            worker.addProperty("avatarExtId",avatarExtId)

            updateWorker(model.id,worker )
        }

        cropImageView.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)
        }
    }

    fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun updateWorker(id:Int, woker: JsonObject){

        showProgessDialog()
        RestClient().getInstance().getRestService().updateDriver(id,woker).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response!!.body().status == 1){
                    showToast("Cập nhật thành công")
                    setResult(100)
                    finish()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }

    override fun resumeData() {

    }

    private fun uploadImage(file:File){
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body =
            MultipartBody.Part.createFormData("image", file.name, requestFile)

        showProgessDialog()
        RestClient().getInstance().getRestService().updateProfile(body).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if( response?.body() != null && response.body().status == 1) {
                    val idOb = response.body().data!!.asJsonObject
                    avatarExtId = idOb["id"].asString
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
