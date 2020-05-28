package com.zamio.adong.ui.ware.stock.stock

import BaseInformationAdapter
import InformationAdapter
import RestClient
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.Information
import com.zamio.adong.model.Profile
import com.zamio.adong.model.WareHouse
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_detail_driver.*
import kotlinx.android.synthetic.main.activity_stock_detail.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStockActivity : BaseActivity() {


    var model: WareHouse? = null
    var modelId = 1
    var keeperId = 1
    override fun getLayout(): Int {
        return R.layout.activity_stock_detail
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {

            modelId = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)
            keeperId = intent.getIntExtra(ConstantsApp.KEY_VALUES_STATUS, 1)

            if (!ConstantsApp.PERMISSION.contains("u")) {
                rightButton.visibility = View.GONE
            }

            if (!ConstantsApp.PERMISSION.contains("d")) {
                tvOk.visibility = View.GONE
            }

            rightButton.setOnClickListener {
//                val intent = Intent(this, UpdateStockActivity::class.java)
//                intent.putExtra(ConstantsApp.KEY_VALUES_ID, model!!)
//                startActivityForResult(intent, 1000)
//                this!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

        getData(modelId)
    }

    fun getKeeperProfile() {
        RestClient().getInstance().getRestService().getUer(keeperId).enqueue(object :
            Callback<RestData<Profile>> {

            override fun onFailure(call: Call<RestData<Profile>>?, t: Throwable?) {

            }
            override fun onResponse(
                call: Call<RestData<Profile>>?,
                response: Response<RestData<Profile>>?
            ) {
                if (response!!.body() != null && response.body().status == 1) {
                    val keeper = response.body().data
                    mList.add(Information("Thủ kho",keeper!!.fullName, ""))
                    mList.add(Information("Số điện thoại",keeper!!.phone, ""))
                    setupRecyclerView(mList)
                }
            }
        })
    }

    override fun resumeData() {

    }

    val mList = ArrayList<Information>()
    private fun getData(id: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getWareHouse(id).enqueue(object :
            Callback<RestData<WareHouse>> {

            override fun onFailure(call: Call<RestData<WareHouse>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<WareHouse>>?,
                response: Response<RestData<WareHouse>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    model = response.body().data ?: return

                    mList.add(Information("Tên",model!!.name, ""))
                    mList.add(Information("Địa chỉ",model!!.address, ""))
                    mList.add(Information("THỦ KHO",model!!.address, "Profile"))
                    getKeeperProfile()

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

    private fun remove() {
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
            getData(modelId)
        }
    }

}
