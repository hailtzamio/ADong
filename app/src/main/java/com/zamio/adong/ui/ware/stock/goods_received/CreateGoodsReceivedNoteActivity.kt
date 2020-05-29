package com.zamio.adong.ui.ware.stock.goods_received

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
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.network.ConstantsApp.*
import com.zamio.adong.ui.ware.stock.stock.StockListActivity
import kotlinx.android.synthetic.main.activity_create_goods_received.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateGoodsReceivedNoteActivity : BaseActivity() {


    var id = 0
    var type = "STOCK"
    var warehouseId = 0
    var warehouseName = ""
    override fun getLayout(): Int {
      return  R.layout.activity_create_goods_received
    }

    override fun initView() {
        tvTitle.text = "Tạo Phiếu Nhập Kho"
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


    }

    override fun initData() {
        productsToGooodReceied.clear()
        lines.clear()

        tvOk.setOnClickListener {

//            if(isEmpty(edtDeliveredBy) || isEmpty(edtModel) || isEmpty(edtCapacity)){
//                showToast("Nhập thiếu thông tin")
//                return@setOnClickListener
//            }

            if(lines.size == 0) {
                showToast("Chọn vật tư")
                return@setOnClickListener
            }

            val request = GoodsNoteRq(edtDeliveredBy.text.toString(),edtNote.text.toString(),edtRef.text.toString(),warehouseId,lines)
            create(request)
        }
    }

    override fun resumeData() {

    }

    private fun setupRecyclerView(){
        val mAdapter = GoodsLines2Adapter(productsToGooodReceied!!)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter
    }

    private fun create(dataOb: GoodsNoteRq){
        showProgessDialog()
        RestClient().getInstance().getRestService().createGoodsReceivedNote(dataOb).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if( response!!.body() != null && response!!.body().status == 1){
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == 101) {
            setupRecyclerView()
        }

        if(resultCode == 100) {
             warehouseId = data!!.getIntExtra("warehouseId", 0)
             warehouseName = data.getStringExtra("warehouseName")!!
            tvChooseWareHouse.text = warehouseName

        }
    }

}
