package com.zamio.adong.ui.product

import RestClient
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.zamio.adong.R
import kotlinx.android.synthetic.main.activity_create_product.*
import kotlinx.android.synthetic.main.item_header_layout.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class CreateProductActivity : BaseActivity() {

    var thumbnailExtId = ""
    override fun getLayout(): Int {
        return R.layout.activity_create_product
    }

    override fun initView() {
        tvTitle.text = "Tạo Vật Tư"
        rightButton.visibility = View.GONE
    }

    override fun initData() {

        tvOk.setOnClickListener {

            if(thumbnailExtId == ""){
                showToast("Chọn ảnh")
                return@setOnClickListener
            }

            if(isEmpty(edtName) || isEmpty(edtType) || isEmpty(edtUnit)){
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            val product = JsonObject()
            product.addProperty("name",edtName.text.toString())
            product.addProperty("type",edtType.text.toString())
            product.addProperty("unit",edtUnit.text.toString())
            product.addProperty("thumbnailExtId",thumbnailExtId)
            createProduct(product)
        }

        cropImageView.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(1,1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)
        }

    }

    override fun resumeData() {

    }

    private fun createProduct(product:JsonObject){
        showProgessDialog()
        RestClient().getRestService().createUser(product).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if( response?.body() != null && response.body().status == 1){
                    showToast("Tạo vật tư thành công")
                    onBackPressed()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }

    private fun uploadImage(file:File){
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

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if( response?.body() != null && response.body().status == 1){
//                    showToast("Tạo vật tư thành công " + response.body().data)
                    val idOb = response.body().data!!.asJsonObject
                    thumbnailExtId = idOb["id"].asString
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
                cropImageView.setImageUriAsync(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}
