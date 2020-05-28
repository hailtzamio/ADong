package com.zamio.adong.ui.ware.stock.goods

import RestClient
import android.content.Intent
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.GoodsNote
import com.zamio.adong.model.GoodsNoteRq
import com.zamio.adong.model.GoodsNoteUpdateRq
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.network.ConstantsApp.*
import com.zamio.adong.ui.ware.stock.StockListActivity
import kotlinx.android.synthetic.main.activity_create_goods_received.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateGoodsReceivedNoteActivity : BaseActivity() {


    var id = 0
    var type = "STOCK"
    var warehouseId = 0
    var warehouseName = ""
    override fun getLayout(): Int {
      return  R.layout.activity_create_goods_received
    }

    override fun initView() {
        tvTitle.text = "Cập Nhật"
        rightButton.visibility = View.GONE
        rlChooseProduct.visibility = View.GONE
        recyclerView.visibility = View.GONE

        rlChooseProduct.setOnClickListener {
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

        val model = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as GoodsNote

        edtDeliveredBy.setText(model.deliveredBy)
        edtRef.setText(model.ref)
        edtNote.setText(model.note)
        tvChooseWareHouse.text = model.warehouseName
        warehouseId = model.warehouseId
        id = model.id

        tvOk.setOnClickListener {

            val addReq = GoodsNoteUpdateRq("")
            addReq.deliveredBy = edtDeliveredBy.text.toString()
            addReq.warehouseId = warehouseId
            addReq.note = edtNote.text.toString()
            addReq.ref = edtRef.text.toString()

            update(addReq)
        }
    }

    override fun resumeData() {

    }

    private fun update(dataOb: GoodsNoteUpdateRq){
        showProgessDialog()
        RestClient().getInstance().getRestService().updateGoodsReceivedNote(id,dataOb).enqueue(object :
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

        }

        if(resultCode == 100) {
             warehouseId = data!!.getIntExtra("warehouseId", 0)
             warehouseName = data.getStringExtra("warehouseName")!!
            tvChooseWareHouse.text = warehouseName
        }
    }

}
