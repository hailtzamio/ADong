package com.zamio.adong.ui.product

import RestClient
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.zamio.adong.R
import com.zamio.adong.model.Product
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_create_product.*
import kotlinx.android.synthetic.main.activity_update_product.cropImageView
import kotlinx.android.synthetic.main.activity_update_product.edtName
import kotlinx.android.synthetic.main.activity_update_product.edtUnit
import kotlinx.android.synthetic.main.activity_update_product.imvImage
import kotlinx.android.synthetic.main.activity_update_product.spinType
import kotlinx.android.synthetic.main.activity_update_product.tvOk
import kotlinx.android.synthetic.main.item_header_layout.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class UpdateProductActivity : BaseActivity() {

    var thumbnailExtId = ""
    var type = "buy"
    override fun getLayout(): Int {
       return R.layout.activity_update_product
    }

    override fun initView() {
        rightButton.visibility = View.GONE
        tvTitle.text = "Cập Nhật"
    }

    override fun initData() {
        val productOb = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as Product
        thumbnailExtId = productOb.thumbnailExtId
        edtName.setText(productOb.name)
        edtUnit.setText(productOb.unit)
        edtCode.setText(productOb.code)

        Picasso.get().load(productOb.thumbnailUrl).into(cropImageView)
        setupChooseSpinner()

        when (productOb.type) {
            "buy"->  spinType.setSelection(0)
            "manufacture"->  spinType.setSelection(1)
            "tool" ->  spinType.setSelection(2)
        }

        tvOk.setOnClickListener {

            if(isEmpty(edtName) || isEmpty(edtUnit)){
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            val product = JsonObject()
            product.addProperty("name",edtName.text.toString())
            product.addProperty("unit",edtUnit.text.toString())
            product.addProperty("type",type)
            product.addProperty("thumbnailExtId",thumbnailExtId)
            product.addProperty("code",edtCode.text.toString())
            updateProduct(productOb.id, product )
        }

        cropImageView.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(1,1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)
        }

        imvImage.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(1,1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)
        }


    }

    private fun setupChooseSpinner(){
        val list: MutableList<String> = ArrayList()
        list.add("Mua tại công trình")
        list.add("Sản xuất")
        list.add("Công cụ")

        val dataAdapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item, list
        )
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinType.adapter = dataAdapter
        spinType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> type = "buy"
                    1 -> type = "manufacture"
                    2 -> type = "tool"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun updateProduct(id:Int, lorry: JsonObject){

        showProgessDialog()
        RestClient().getInstance().getRestService().updateProduct(id,lorry).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response.body().status == 1){
                    showToast("Cập nhật thành công")
                    setResult(100)
                    finish()
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
                cropImageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }



}
