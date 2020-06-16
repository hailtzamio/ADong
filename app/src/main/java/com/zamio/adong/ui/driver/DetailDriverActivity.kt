package com.zamio.adong.ui.driver

import InformationAdapter
import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.model.Driver
import com.zamio.adong.model.Information
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.activity.PreviewImageActivity
import kotlinx.android.synthetic.main.activity_detail_driver.*
import kotlinx.android.synthetic.main.activity_detail_driver.recyclerView
import kotlinx.android.synthetic.main.activity_detail_driver.tvOk
import kotlinx.android.synthetic.main.activity_transport_detail.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailDriverActivity : BaseActivity() {


    var model: Driver? = null
    var modelId = 1
    val mList = ArrayList<Information>()
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

                    mList.add(Information("Tên",model!!.fullName ?: "---", ""))
                    if (model!!.workingStatus == "idle") {
                        mList.add(Information("Trạng thái","Đang rảnh", ""))
                    } else {
                        mList.add(Information("Trạng thái","Đang bận", ""))
                    }

                    mList.add(Information("Số điện thoại",model!!.phone ?: "---", ""))
                    mList.add(Information("Số điện thoại 2",model!!.phone2 ?: "---", ""))
                    mList.add(Information("Email",model!!.email ?: "---", ""))

                    Picasso.get().load(model!!.avatarUrl).into(cropImageView)


                    setupRecyclerView(mList)
                }
            }
        })
    }

    private fun setupRecyclerView(data: List<Information>) {
        val mAdapter = InformationAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter
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
