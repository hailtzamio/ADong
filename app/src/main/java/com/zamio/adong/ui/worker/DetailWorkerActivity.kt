package com.zamio.adong.ui.worker

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
import com.zamio.adong.model.Worker
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.activity.PreviewImageActivity
import kotlinx.android.synthetic.main.activity_detail_worker.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailWorkerActivity : BaseActivity() {


    var product: Worker? = null
    var productId = 1
    override fun getLayout(): Int {
        return R.layout.activity_detail_worker
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {

            productId = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)


            if (!ConstantsApp.PERMISSION.contains("u")) {
                rightButton.visibility = View.GONE
            }

            if (!ConstantsApp.PERMISSION.contains("d")) {
                tvOk.visibility = View.GONE
            }

            rightButton.setOnClickListener {
                val intent = Intent(this, UpdateWorkerActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, product!!)
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
                builder.setMessage("Xóa công nhân này?")
                    .setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show()
            }

            cropImageView.setOnClickListener {
                if (product!!.avatarUrl != null) {
                    val intent = Intent(this, PreviewImageActivity::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_ID, product!!.avatarUrl)
                    startActivityForResult(intent, 1000)
                }
            }

            tvTeam.setOnClickListener {
                //                val intent = Intent(this, DetailTeamActivity::class.java)
//                intent.putExtra(ConstantsApp.KEY_QUESTION_ID, product.id)
//                startActivity(intent)
//               overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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
        RestClient().getInstance().getRestService().getWorker(id).enqueue(object :
            Callback<RestData<Worker>> {

            override fun onFailure(call: Call<RestData<Worker>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<Worker>>?,
                response: Response<RestData<Worker>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    product = response.body().data ?: return
                    tvName.text = product!!.fullName
                    tvPhone.text = product!!.phone
                    tvAddress.text = product!!.address


                    if (product!!.lineId != null && product!!.lineId != "") {
                        tvEmail.text = product!!.lineId
                    }

                    if (product!!.bankName != null && product!!.bankName != "") {
                        tvBankName.text = product!!.bankName
                    }

                    if (product!!.bankAccount != null && product!!.bankAccount != "") {
                        tvBankAccount.text = product!!.bankAccount
                    }

                    if (product!!.teamName != null && product!!.teamName != "") {
                        tvTeam.text = product!!.teamName
                    }

                    if (product!!.isTeamLeader) {
                        tvPosition.text = "Đội trưởng"
                    } else {
                        tvPosition.text = "Công nhân"
                    }

                    if (product!!.workingStatus == "idle") {
                        tvStatus.text = "Đang rảnh"
                    } else {
                        tvStatus.text = "Đang bận"
                    }

                    if (product!!.avatarUrl != null) {
                        Picasso.get().load(product!!.avatarUrl).error(R.drawable.ava)
                            .into(cropImageView)
                    }

                }
            }
        })
    }

    private fun removeLorry() {
        showProgessDialog()
        RestClient().getInstance().getRestService().removeWorker(product!!.id).enqueue(object :
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
