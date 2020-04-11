package com.zamio.adong.ui.contractor

import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.Contractor
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_detail_contractor.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailContractorActivity : BaseActivity() {


    var product: Contractor? = null
    var productId = 1
    override fun getLayout(): Int {
        return R.layout.activity_detail_contractor
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_QUESTION_ID)) {

            productId = intent.getIntExtra(ConstantsApp.KEY_QUESTION_ID, 1)


            if (!ConstantsApp.PERMISSION.contains("u")) {
                rightButton.visibility = View.GONE
            }

            if (!ConstantsApp.PERMISSION.contains("d")) {
                tvOk.visibility = View.GONE
            }

            rightButton.setOnClickListener {
                val intent = Intent(this, UpdateContractorActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_QUESTION_ID, product!!)
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
                builder.setMessage("Xóa Nhà thầu phụ này?")
                    .setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show()
            }

        }

        if (intent.hasExtra(ConstantsApp.ChooseTeamWorkerActivity)) {
            tvOk.visibility = View.GONE
            rightButton.visibility = View.GONE
        }

        getProduct(productId)
    }

    override fun resumeData() {

    }

    private fun getProduct(id: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getContractor(id).enqueue(object :
            Callback<RestData<Contractor>> {

            override fun onFailure(call: Call<RestData<Contractor>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<Contractor>>?,
                response: Response<RestData<Contractor>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    product = response.body().data ?: return
                    tvName.text = product!!.name
                    tvPhone.text = product!!.phone
                    tvAddress.text = product!!.address + " - " + product!!.districtName + " - " + product!!.provinceName
                    tvEmail.text = product!!.email
                    tvProjectName.text = product!!.projectName
                    tvRating.text = product!!.rating.toString() + " *"

                    if (product!!.workingStatus == "idle") {
                        tvStatus.text = "Đang rảnh"
                    } else {
                        tvStatus.text = "Đang bận"
                    }
                }
            }
        })
    }

    private fun removeLorry() {
        showProgessDialog()
        RestClient().getInstance().getRestService().removeContractor(product!!.id).enqueue(object :
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
            getProduct(productId)
        }
    }

}
