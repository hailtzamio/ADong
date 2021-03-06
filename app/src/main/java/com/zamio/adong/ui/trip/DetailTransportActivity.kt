package com.zamio.adong.ui.trip

import BaseInformationAdapter
import InformationAdapter
import RestClient
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.ui.network.UserRoles
import com.google.gson.JsonElement
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.zamio.adong.R
import com.zamio.adong.model.Information
import com.zamio.adong.model.Profile
import com.zamio.adong.model.Transport
import com.zamio.adong.model.WareHouse
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.utils.Utils
import kotlinx.android.synthetic.main.activity_transport_detail.*
import kotlinx.android.synthetic.main.item_header_layout.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetailTransportActivity : BaseActivity() {


    var model: Transport? = null
    var id = 1
    var status = 0
    override fun getLayout(): Int {
        return R.layout.activity_transport_detail
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {

            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)


            if (!ConstantsApp.PERMISSION.contains("u")) {
                rightButton.visibility = View.GONE
            }

            rightButton.visibility = View.GONE
            tvOk.setOnClickListener {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
            }
        }



        getData(id)
    }

    override fun resumeData() {

    }

    val mList = ArrayList<Information>()
    val mListProduct = ArrayList<Information>()
    private fun getData(id: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getTransport(id).enqueue(object :
            Callback<RestData<Transport>> {

            override fun onFailure(call: Call<RestData<Transport>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<Transport>>?,
                response: Response<RestData<Transport>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    model = response.body().data ?: return
                    status = model!!.status ?: 1
                    mList.add(Information("Code",model!!.code ?: "---", ""))
                    mList.add(Information("Ngày dự kiến",Utils.convertDate(model!!.plannedDatetime) ?: "---", ""))
                    mList.add(Information("Kho / Xưởng",model!!.warehouseName ?: "---", ""))
                    mList.add(Information("Địa chỉ Kho / Xưởng",model!!.warehouseAddress ?: "---", ""))
                    mList.add(Information("Tên dự án",model!!.projectName ?: "---", ""))
                    mList.add(Information("Địa chỉ dự án",model!!.projectAddress ?: "---", ""))

                    setupRecyclerView(mList)

                    if(model!!.lines != null) {
                        model!!.lines!!.forEach {
                            mListProduct.add(Information(it.quantity.toString() + " " + it.productUnit, it.productName, ""))
                        }
                    }

                    setupRecyclerViewSmall(mListProduct)
                    if (ConstantsApp.USER_ROLES.contains(UserRoles.Driver.type)) {
                        if (status == 4) {
                            tvOk.visibility = View.VISIBLE
                        }

                        if (status == 5) {
                            tvOk.visibility = View.VISIBLE
                            tvOk.text = "GIAO HÀNG"
                        }
                    }
                }
            }
        })
    }

    private fun setupRecyclerView(data: ArrayList<Information>) {
        val mAdapter = InformationAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter
    }

    private fun setupRecyclerViewSmall(data: ArrayList<Information>) {
        val mAdapter = InformationAdapter(data)
        recyclerView2.layoutManager = LinearLayoutManager(this)
        recyclerView2.setHasFixedSize(false)
        recyclerView2.adapter = mAdapter
    }

    private fun pickup() {
        showProgessDialog()
        RestClient().getInstance().getRestService().transportPickUp(id).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    showToast("Thành công")
                    setResult(100)
                    finish()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }

    private fun unload() {
        showProgessDialog()
        RestClient().getInstance().getRestService().transportUnload(id).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    showToast("Thành công")
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
        RestClient().getRestService().transportUpload(id,body).enqueue(object :
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
                    if(status == 4) {
                        pickup()
                    } else {
                        unload()
                    }

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

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

}
