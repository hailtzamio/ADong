package com.zamio.adong.ui.ware.stock.goods_issue

import GoodsLines2Adapter
import RestClient
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.GoodsNoteRq
import com.zamio.adong.model.GoodsNoteUpdateRq
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.network.ConstantsApp.*
import com.zamio.adong.ui.project.MainProjectActivity
import com.zamio.adong.ui.ware.stock.goods_received.AddProductToGoodsReceiedActivity
import com.zamio.adong.ui.ware.stock.stock.StockListActivity
import kotlinx.android.synthetic.main.activity_create_goods_issue.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateGoodsIssueActivity : BaseActivity() {


    var id = 0
    var type = "STOCK"
    var warehouseId = 0
    var projectId = 0
    var note = ""
    var reason = ""
    var receiver = ""
    var projectName = ""
    var warehouseName = ""

    override fun getLayout(): Int {
        return R.layout.activity_create_goods_issue
    }

    override fun initView() {
        tvTitle.text = "Tạo Phiếu Xuất Kho"
        rightButton.visibility = View.GONE

        tvProduct.setOnClickListener {
            val intent = Intent(this, AddProductToGoodsReceiedActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, 0)
            startActivityForResult(intent, 1000)
        }

        rlChooseWareHouse.setOnClickListener {
            val intent = Intent(this, StockListActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, 0)
            startActivityForResult(intent, 1000)
        }

        rlChooseProject.setOnClickListener {
            val intent = Intent(this, MainProjectActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, 0)
            startActivityForResult(intent, 1000)
        }
    }

    override fun initData() {
        productsToGooodReceied.clear()
        lines.clear()

        tvOk.setOnClickListener {

            if (projectId == 0) {
                showToast("Chọn công trình")
                return@setOnClickListener
            }

            if (warehouseId == 0) {
                showToast("Chọn kho")
                return@setOnClickListener
            }

            if (lines.size == 0) {
                showToast("Chọn vật tư")
                return@setOnClickListener
            }

            val request = GoodsNoteUpdateRq("")
            request.receiver = edtReceivedBy.text.toString()
            request.note = edtNote.text.toString()
            request.reason = edtReason.text.toString()
            request.warehouseId = warehouseId
            request.projectId = projectId
            request.linesAddNew = lines
            create(request)
        }
    }

    override fun resumeData() {

    }

    private fun setupRecyclerView() {
        val mAdapter = GoodsLines2Adapter(productsToGooodReceied!!)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter
    }

    private fun create(dataOb: GoodsNoteUpdateRq) {
        showProgessDialog()
        RestClient().getInstance().getRestService().createGoodsIssueDocument(dataOb)
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
                        setResult(100)
                        finish()
                    } else {
                        val obj = JSONObject(response.errorBody().string())
                        showToast(obj["message"].toString())
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 101) {
            setupRecyclerView()
        }

        if (resultCode == 100) {
            warehouseId = data!!.getIntExtra("warehouseId", 0)
            warehouseName = data.getStringExtra("warehouseName")!!
            tvChooseWareHouse.text = warehouseName
        }

        if (resultCode == 102 && data != null) {
            projectId = data.getIntExtra("projectId", 0)
            projectName = data.getStringExtra("projectName")!!
            tvChooseProject.text = projectName
        }
    }

}
