package com.zamio.adong.ui.contractor

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
import com.zamio.adong.R
import com.zamio.adong.model.Contractor
import com.zamio.adong.model.Information
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_detail_contractor.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailContractorActivity : BaseActivity() {


    var product: Contractor? = null
    var id = 0
    var regId = 0
    var isFromProjectRegister = false
    val mList = ArrayList<Information>()
    override fun getLayout(): Int {
        return R.layout.activity_detail_contractor
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

            if (!ConstantsApp.PERMISSION.contains("d")) {
                tvOk.visibility = View.GONE
            }

            rightButton.setOnClickListener {
                val intent = Intent(this, UpdateContractorActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, product!!)
                startActivityForResult(intent, 1000)
                this!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }


            var title = "Xóa Nhà thầu phụ này?"

            if (intent.hasExtra(ConstantsApp.KEY_VALUES_HIDE)) {

                rightButton.visibility = View.GONE

                regId = intent.getIntExtra(ConstantsApp.KEY_VALUES_HIDE, 1)
                val status = intent.getStringExtra(ConstantsApp.KEY_VALUES_STATUS) ?: ""

                if(status == "APPROVED" || status == "REJECTED") {
                    tvOk.visibility = View.GONE
                }

                isFromProjectRegister = true
                title = "Chọn Nhà thầu phụ này?"
                tvOk.text = "ĐỒNG Ý"
            }

            tvOk.setOnClickListener {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                if(isFromProjectRegister) {
                                    approveRegisterProject()
                                } else {
                                    remove()
                                }

                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage(title)
                    .setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show()
            }


        }

        if (intent.hasExtra(ConstantsApp.ChooseTeamWorkerActivity)) {
            tvOk.visibility = View.GONE
            rightButton.visibility = View.GONE
        }

        getProduct(id)
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

                    mList.clear()

                    var address = "---"

                    if(product!!.address != null && product!!.address != "") {
                        address =  product!!.address.toString()
                    }

                    if(product!!.districtName != null && product!!.districtName == "") {
                        address = address + " - " + product!!.districtName
                    }

                    if(product!!.provinceName != null && product!!.provinceName == "") {
                        address = address + " - " + product!!.provinceName
                    }

                    mList.add(Information("Tên",product!!.name ?: "---", ""))
                    mList.add(Information("Số điện thoại",product!!.phone ?: "---", ""))
                    mList.add(Information("Email",product!!.email ?: "---", ""))
                    mList.add(Information("Địa chỉ",address, ""))
                    if (product!!.workingStatus == "idle") {
                        mList.add(Information("Trạng thái","Đang rảnh", ""))
                    } else {
                        mList.add(Information("Trạng thái","Đang bận", ""))
                    }

                    mList.add(Information("Tên dự án",product!!.projectName ?: "---", ""))

                    if (product!!.rating != null) {
                        rating.rating = product!!.rating!!
                    }

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

    private fun approveRegisterProject() {

        showProgessDialog()
        RestClient().getInstance().getRestService().approveRegisterProject(regId)
            .enqueue(object :
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
                        onBackPressed()
                    } else {
                        if (response.errorBody() != null) {
                            val obj = JSONObject(response.errorBody().string())
                            showToast(obj["message"].toString())
                        }
                    }
                }
            })
    }

    private fun remove() {
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
            getProduct(id)
        }
    }

}
