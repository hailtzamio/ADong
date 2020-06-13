package com.zamio.adong.ui.ware.stock.goods_issue

import GoodsIssueLinesAdapter
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
import com.zamio.adong.model.*
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.ware.stock.goods_received.AddProductToGoodsReceiedActivity
import com.zamio.adong.ui.ware.stock.goods_received.UpdateGoodsReceivedNoteActivity
import kotlinx.android.synthetic.main.activity_detail_goods_received.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailGoodsIssueActivity : BaseActivity() {


    var data: GoodsIssue? = null
    var id = 1
    var receiver: String = ""
    var projectId: Int = 0
    var warehouseId: Int = 0
    var note: String = ""
    var reason: String = ""
    var status = ""
    val linesAddNew = ArrayList<LinesAddNew>()
    override fun getLayout(): Int {
        return R.layout.activity_detail_goods_issue
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);
        rightButton.visibility = View.GONE
        rlAddProduct.setOnClickListener {
            val intent = Intent(this, AddProductToGoodsReceiedActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, 0)
            startActivityForResult(intent, 1000)
        }

        rightButton.setOnClickListener {
            val intent = Intent(this, UpdateGoodsIssueActivity::class.java)
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

            tvOk.text = "XÁC NHẬN XUẤT KHO"
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
        RestClient().getInstance().getRestService().getGoodsIssueDocument(id).enqueue(object :
            Callback<RestData<GoodsIssue>> {

            override fun onFailure(call: Call<RestData<GoodsIssue>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<GoodsIssue>>?,
                response: Response<RestData<GoodsIssue>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    data = response.body().data ?: return

                    if (data != null) {

                        receiver = data!!.receiver.toString()
                        if (data!!.projectId != null) {
                            projectId = data!!.projectId!!
                        }
                        note = data!!.note.toString()
                        reason = data!!.reason.toString()

                        warehouseId = data!!.warehouseId!!

                        if (data!!.code != null && data!!.code != "") {
                            tvName.text = data!!.code
                        }

                        if (data!!.receiver != null && data!!.receiver != "") {
                            tvAuthor.text = data!!.receiver
                        }

                        if (data!!.reason != null && data!!.reason != "") {
                            tvDate.text = data!!.reason
                        }

                        if (data!!.note != null && data!!.note != "") {
                            tvEditDate.text = data!!.note
                        }

                        if (data!!.warehouseName != null && data!!.warehouseName != "") {
                            tvEditer.text = data!!.warehouseName
                        }

                        if (data!!.status != null && data!!.status != "") {
                            if (data!!.status == "DONE") {
                                tvStatus.text = "Hoàn thành"
                                tvOk.visibility = View.GONE
                                tvAddProduct.text = "DANH SÁCH VẬT TƯ"
                                rlAddProduct.isEnabled = false
                                rightButton.visibility = View.GONE
                            } else {
                                tvStatus.text = "Nháp"
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

    var lineList = ArrayList<GoodsIssueLine>()
    val mAdapter = GoodsIssueLinesAdapter(lineList)
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

        if (status == "DRAFT") {
            val swipeHandler = object : SwipeToDeleteCallback(this!!) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = recyclerView.adapter as GoodsIssueLinesAdapter
                    adapter.removeAt(viewHolder.adapterPosition)
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    private fun confirm() {
        showProgessDialog()
        RestClient().getInstance().getRestService().confirmGoodsIssueDocument(data!!.id)
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
                        getData(id)
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

    private fun removeProduct(id: Int) {

        val removeReq = GoodsNoteUpdateRq("")
        removeReq.receiver = receiver
        removeReq.projectId = projectId
        removeReq.warehouseId = warehouseId
        removeReq.note = note
        removeReq.reason = reason

        val mRemoveList = ArrayList<Int>()
        mRemoveList.add(id)

        removeReq.linesDelete = mRemoveList
        doUpdateApi(removeReq)

    }

    private fun addProduct() {

        val addReq = GoodsNoteUpdateRq("")
        addReq.receiver = receiver
        addReq.projectId = projectId

        addReq.note = note
        addReq.reason = reason

        addReq.linesAddNew.addAll(ConstantsApp.lines)

        doUpdateApi(addReq)

    }

    private fun doUpdateApi(removeReq: GoodsNoteUpdateRq) {

        if (status == "DONE") {
            showToast("Không xóa được, trạng thái đã hoàn thành")
            return
        }

        showProgessDialog()
        RestClient().getInstance().getRestService().updateGoodsIssueDocument(data!!.id, removeReq)
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
                        getData(id)
                        showToast("Thành công")
                    } else {
                        if (response.errorBody() != null) {
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

        if (resultCode == 101) {
//            lineList.addAll(ConstantsApp.lines)
//            mAdapter.notifyDataSetChanged()
            addProduct()
            Log.e("hailpt~~", " line > " + ConstantsApp.lines.size)
            ConstantsApp.lines.clear()

        }
    }

}
