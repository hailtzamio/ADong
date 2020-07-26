package com.zamio.adong.ui.worker

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
import com.zamio.adong.model.Information
import com.zamio.adong.model.Worker
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.activity.PreviewImageActivity
import kotlinx.android.synthetic.main.activity_detail_worker.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailWorkerActivity : BaseActivity() {


    var worker: Worker? = null
    var productId = 1
    val mList = ArrayList<Information>()
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
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, worker!!)
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
                if (worker!!.avatarUrl != null) {
                    val intent = Intent(this, PreviewImageActivity::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_ID, worker!!.avatarUrl)
                    startActivityForResult(intent, 1000)
                }
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
                    worker = response.body().data ?: return

                    mList.clear()

                    updateObject(worker!!)

                    mList.add(Information("Tên", worker!!.fullName ?: "---", ""))
                    mList.add(Information("Tên đăng nhập", worker!!.username ?: "---", ""))
                    if (worker!!.isTeamLeader) {
                        mList.add(Information("Chức danh", "Đội trưởng", ""))
                    } else {
                        mList.add(Information("Chức danh", "Công nhân", ""))
                    }

                    if (worker!!.workingStatus == "idle") {
                        mList.add(Information("Trạng thái", "Đang rảnh", ""))
                    } else {
                        mList.add(Information("Trạng thái", "Đang bận", ""))
                    }

                    mList.add(Information("Số điện thoại", worker!!.phone ?: "---", ""))
                    mList.add(Information("Số điện thoại 2", worker!!.phone2 ?: "---", ""))
                    mList.add(Information("Địa chỉ", worker!!.address ?: "---", ""))
                    mList.add(Information("Line ID", worker!!.lineId ?: "---", ""))
                    mList.add(Information("Tên đội", worker!!.teamName ?: "---", ""))
                    mList.add(Information("Ngân hàng", worker!!.bankName ?: "---", ""))
                    mList.add(Information("Số tài khoản", worker!!.bankAccount ?: "---", ""))





                    if (worker!!.avatarUrl != null) {
                        Picasso.get().load(worker!!.avatarUrl).error(R.drawable.ava)
                            .into(cropImageView)
                    }

                    setupRecyclerView(mList)

                }
            }
        })
    }

    private fun updateObject(worker:Worker) {

        if(worker.lineId == "") {
            worker.lineId = null
        }

        if(worker.address == "") {
            worker.address = null
        }

        if(worker.bankName == "") {
            worker.bankName = null
        }

        if(worker.phone2 == "") {
            worker.phone2 = null
        }

        if(worker.teamName == "") {
            worker.teamName = null
        }

        if(worker.bankAccount == "") {
            worker.bankAccount = null
        }

    }

    private fun setupRecyclerView(data: List<Information>) {
        val mAdapter = InformationAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter
    }

    private fun removeLorry() {
        showProgessDialog()
        RestClient().getInstance().getRestService().removeWorker(worker!!.id).enqueue(object :
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
