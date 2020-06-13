package com.zamio.adong.ui.ware.stock.goods_received

import GoodsLinesAdapter
import RestClient
import SwipeToDeleteCallback
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.GoodsNote
import com.zamio.adong.model.GoodsNoteUpdateRq
import com.zamio.adong.model.LinesAddNew
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_detail_goods_received.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailGoodsReveivedActivity : BaseActivity() {


    var data: GoodsNote? = null
    var id = 1
    var deliveredBy:String = ""
    var warehouseId:Int = 0
    var note:String = ""
    var ref:String = ""
    var status = ""
    val linesAddNew = ArrayList<LinesAddNew>()
    override fun getLayout(): Int {
        return R.layout.activity_detail_goods_received
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);

        rlAddProduct.setOnClickListener {
            val intent = Intent(this, AddProductToGoodsReceiedActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, 0)
            startActivityForResult(intent, 1000)
        }

        rightButton.visibility = View.GONE
        rightButton.setOnClickListener {
            val intent = Intent(this, UpdateGoodsReceivedNoteActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, data)
            startActivityForResult(intent, 1000)

        }
    }

    override fun initData() {
        ConstantsApp.lines.clear()
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {

            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)


            if (!ConstantsApp.PERMISSION.contains("u")) {
                rightButton.visibility = View.GONE
            }

            if (!ConstantsApp.PERMISSION.contains("d")) {
                tvOk.visibility = View.GONE
            }

            tvOk.setOnClickListener {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                confirm()
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage("Xác nhận?")
                    .setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show()
            }

        }

        getData(id)

    }

    override fun resumeData() {

    }

    private fun getData(id: Int) {

        lineList.clear()

        showProgessDialog()
        RestClient().getInstance().getRestService().getGoodsReceivedNote(id).enqueue(object :
            Callback<RestData<GoodsNote>> {

            override fun onFailure(call: Call<RestData<GoodsNote>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<GoodsNote>>?,
                response: Response<RestData<GoodsNote>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    data = response.body().data ?: return

                    if (data != null) {

                        deliveredBy = data!!.deliveredBy.toString()
                        warehouseId = data!!.warehouseId
                        note = data!!.note.toString()
                        ref = data!!.ref.toString()

                        if (data!!.code != null && data!!.code != "") {
                            tvName.text = data!!.code
                        }

                        if (data!!.deliveredBy != null && data!!.deliveredBy != "") {
                            tvAuthor.text = data!!.deliveredBy
                        }

                        if (data!!.ref != null && data!!.ref != "") {
                            tvDate.text = data!!.ref
                        }

                        if (data!!.note != null && data!!.note != "") {
                            tvEditDate.text = data!!.note
                        }

                        if (data!!.warehouseName != null && data!!.warehouseName != "") {
                            tvEditer.text = data!!.warehouseName
                        }

                        if (data!!.status != null && data!!.status != "") {
                            if(data!!.status == "DONE") {
                                tvStatus.text = "Hoàn thành"
                                tvOk.visibility = View.GONE
                                tvAddProduct.text = "DANH SÁCH VẬT TƯ"
                                rlAddProduct.isEnabled = false
                                rightButton.visibility = View.GONE
                            } else {
                                tvStatus.text = "Nháp"
                                tvOk.visibility = View.GONE
                            }
                            status = data!!.status!!
                        }

                        if (data!!.lines != null) {
                            lineList.addAll(data!!.lines!!)
                            setupRecyclerView()
                        }
                    }
                }
            }
        })
    }

    var lineList =  ArrayList<LinesAddNew>()
    val mAdapter = GoodsLinesAdapter(lineList)
    private fun setupRecyclerView() {

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            //            val intent = Intent(this, DetailCriteriaActivity::class.java)
//            intent.putExtra(ConstantsApp.KEY_QUESTION_ID, product.id)
//            startActivityForResult(intent,1000)
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        mAdapter.onRemoveItem = {
            removeProduct(it)
        }

//        if(status == "DRAFT") {
//            val swipeHandler = object : SwipeToDeleteCallback(this) {
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                    val adapter = recyclerView.adapter as GoodsLinesAdapter
//                    adapter.removeAt(viewHolder.adapterPosition)
//                }
//            }
//            val itemTouchHelper = ItemTouchHelper(swipeHandler)
//            itemTouchHelper.attachToRecyclerView(recyclerView)
//        }
    }

    private fun confirm() {
        showProgessDialog()
        RestClient().getInstance().getRestService().confirmGoodsReceivedNote(data!!.id).enqueue(object :
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
                    getData(id)
                    showToast("Thành công")
                    onBackPressed()
                } else {
                    if(response.errorBody() != null) {
                        val obj = JSONObject(response.errorBody().string())
                        showToast(obj["message"].toString())
                    }
                }
            }
        })
    }

    private fun removeProduct(id:Int) {

        val removeReq = GoodsNoteUpdateRq("")
        removeReq.deliveredBy = deliveredBy
        removeReq.warehouseId = warehouseId
        removeReq.note = note
        removeReq.ref = ref

        val mRemoveList = ArrayList<Int>()
        mRemoveList.add(id)

        removeReq.linesDelete = mRemoveList

        doUpdateApi(removeReq)
    }

    private fun addProduct() {

        val addReq = GoodsNoteUpdateRq("")
        addReq.deliveredBy = deliveredBy
        addReq.warehouseId = warehouseId
        addReq.note = note
        addReq.ref = ref

        addReq.linesAddNew.addAll(ConstantsApp.lines)

        doUpdateApi(addReq)
    }

    private fun doUpdateApi(removeReq:GoodsNoteUpdateRq) {

        if(status == "DONE") {
            showToast("Không xóa được, trạng thái đã hoàn thành")
            return
        }

        showProgessDialog()
        RestClient().getInstance().getRestService().updateGoodsReceivedNote(data!!.id,removeReq).enqueue(object :
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
                    getData(id)
                    showToast("Thành công")
                } else {
                    if(response.errorBody() != null) {
                        val obj = JSONObject(response.errorBody().string())
                        showToast(obj["message"].toString())
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 100) {
            getData(id)
        }

        if(resultCode == 101) {
//            lineList.addAll(ConstantsApp.lines)
//            mAdapter.notifyDataSetChanged()
            addProduct()
            Log.e("hailpt~~", " line > " + ConstantsApp.lines.size)
            ConstantsApp.lines.clear()

        }
    }

}
