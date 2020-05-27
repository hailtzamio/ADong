package com.zamio.adong.ui.driver

import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.model.Driver
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.activity.PreviewImageActivity
import kotlinx.android.synthetic.main.activity_detail_driver.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailDriverActivity : BaseActivity() {


    var model: Driver? = null
    var modelId = 1
    override fun getLayout(): Int {
        return R.layout.activity_detail_driver
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {

            modelId = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)

            if (!ConstantsApp.PERMISSION.contains("u")) {
                rightButton.visibility = View.GONE
            }

            if (!ConstantsApp.PERMISSION.contains("d")) {
                tvOk.visibility = View.GONE
            }

            rightButton.setOnClickListener {
                val intent = Intent(this, UpdateDriverActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, model!!)
                startActivityForResult(intent, 1000)
                this!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            tvOk.setOnClickListener {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                removeLorry()
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage("Xóa lái xe này?")
                    .setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show()
            }

            cropImageView.setOnClickListener {
                if (model!!.avatarUrl != null) {
                    val intent = Intent(this, PreviewImageActivity::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_ID, model!!.avatarUrl)
                    startActivityForResult(intent, 1000)
                }
            }
        }

        getProduct(modelId)
    }

    override fun resumeData() {

    }

    private fun getProduct(id: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getDriver(id).enqueue(object :
            Callback<RestData<Driver>> {

            override fun onFailure(call: Call<RestData<Driver>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<Driver>>?,
                response: Response<RestData<Driver>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    model = response.body().data ?: return
                    tvName.text = model!!.fullName
                    tvPhone.text = model!!.phone

                    if(model!!.phone2 != null) {
                        tvPhone2.text = model!!.phone2
                    }

                    if(model!!.email != null) {
                        tvEmail.text = model!!.email
                    }

                    if (model!!.workingStatus == "idle") {
                        tvStatus.text = "Đang rảnh"
                    } else {
                        tvStatus.text = "Đang bận"
                    }

                    Picasso.get().load(model!!.avatarUrl).into(cropImageView)
                }
            }
        })
    }

    private fun removeLorry() {
        showProgessDialog()
        RestClient().getInstance().getRestService().removeDriver(model!!.id).enqueue(object :
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
                    showToast("Xóa thành công")
                    setResult(100)
                    finish()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 100) {
            getProduct(modelId)
        }
    }

}
