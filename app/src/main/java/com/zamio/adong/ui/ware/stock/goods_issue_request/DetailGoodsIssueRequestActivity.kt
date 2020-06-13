package com.zamio.adong.ui.ware.stock.goods_issue_request

import GoodsIssueRequestLinesAdapter
import RestClient
import SwipeToDeleteCallback
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import kotlinx.android.synthetic.main.activity_detail_goods_issue_request.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener

class DetailGoodsIssueRequestActivity : BaseActivity() {


    var data: GoodsIssueRequest? = null
    var id = 1
    var plannedDatetime: String = ""
    var productReqId: Int = 0
    var warehouseId: Int = 0
    var note: String = ""
    var status = 0

    val linesAddNew = ArrayList<LinesAddNew>()
    override fun getLayout(): Int {
        return R.layout.activity_detail_goods_issue_request
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);

        rlAddProduct.setOnClickListener {
//            val intent = Intent(this, AddProductToGoodsReceiedActivity::class.java)
//            intent.putExtra(ConstantsApp.KEY_VALUES_ID, 0)
//            startActivityForResult(intent, 1000)
        }

        rightButton.visibility = View.GONE
        rightButton.setOnClickListener {
            //            val intent = Intent(this, UpdateGoodsIssueActivity::class.java)
//            intent.putExtra(ConstantsApp.KEY_VALUES_ID, data)
//            startActivityForResult(intent, 1000)

        }

        tvEdit.setOnClickListener {
            showDateTimePicker()
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
        RestClient().getInstance().getRestService().getGoodsIssueRequest(id).enqueue(object :
            Callback<RestData<GoodsIssueRequest>> {

            override fun onFailure(call: Call<RestData<GoodsIssueRequest>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<GoodsIssueRequest>>?,
                response: Response<RestData<GoodsIssueRequest>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    data = response.body().data ?: return

                    if (data != null) {

                        plannedDatetime = data!!.plannedDatetime.toString()

                        if (data!!.productReqId != null) {
                            productReqId = data!!.productReqId!!
                        }

                        if (data!!.warehouseId != null) {
                            warehouseId = data!!.warehouseId!!
                        }
                        note = data!!.note.toString()


                        if (data!!.code != null && data!!.code != "") {
                            tvName.text = data!!.code
                        }

                        if (data!!.status != null) {
                            tvStatus.text = data!!.status.toString()
                        }
//
                        if (data!!.plannedDatetime != null && data!!.plannedDatetime != "") {
                            tvDate.text = data!!.plannedDatetime
                        }
//
                        if (data!!.note != null && data!!.note != "") {
                            tvEditDate.text = data!!.note
                        }
//
                        if (data!!.warehouseName != null && data!!.warehouseName != "") {
                            tvEditer.text = data!!.warehouseName
                        }

//                        if (data!!.status != null && data!!.status != "") {
//                            if (data!!.status == "DONE") {
//                                tvStatus.text = "Hoàn thành"
//                                tvOk.visibility = View.GONE
//                                tvAddProduct.text = "DANH SÁCH VẬT TƯ"
//                                rlAddProduct.isEnabled = false
//                                rightButton.visibility = View.GONE
//                            } else {
//                                tvStatus.text = "Nháp"
//                            }
//                            status = data!!.status!!
//                        }

                        if (data!!.lines != null) {
                            lineList.addAll(data!!.lines!!)
                            setupRecyclerView()
                        }
                    }
                }
            }
        })
    }

    var lineList = ArrayList<GoodsIssueRequestLine>()
    val mAdapter = GoodsIssueRequestLinesAdapter(lineList)
    private fun setupRecyclerView() {

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            showToast("onItemClick")
        }

        mAdapter.onRemoveItem = {
            removeProduct(it)
        }

        if (status != 1) {
            val swipeHandler = object : SwipeToDeleteCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = recyclerView.adapter as GoodsIssueRequestLinesAdapter
                    adapter.removeAt(viewHolder.adapterPosition)
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    private fun removeProduct(id: Int) {

        val removeReq = GoodsNoteUpdateRq("")
        removeReq.plannedDatetime = plannedDatetime
        removeReq.productReqId = productReqId
        removeReq.warehouseId = warehouseId
        removeReq.note = note

        val mRemoveList = ArrayList<Int>()
        mRemoveList.add(id)

        removeReq.linesDelete = mRemoveList
        doUpdateApi(removeReq)

    }

    private fun updatePlanDate() {
        val addReq = GoodsNoteUpdateRq("")
        addReq.plannedDatetime = plannedDatetime
        addReq.productReqId = productReqId
        addReq.warehouseId = warehouseId
        addReq.note = note
        doUpdateApi(addReq)
    }

    private fun addProduct() {

        val addReq = GoodsNoteUpdateRq("")
        addReq.plannedDatetime = plannedDatetime
        addReq.productReqId = productReqId
        addReq.warehouseId = warehouseId
        addReq.note = note

        ConstantsApp.lines.forEach {
            it.productReqLineId = it.productId
        }

        addReq.linesAddNew.addAll(ConstantsApp.lines)
        doUpdateApi(addReq)
    }

    private fun doUpdateApi(removeReq: GoodsNoteUpdateRq) {

        if (status == 1) {
            showToast("Không xóa được, trạng thái đã hoàn thành")
            return
        }

        showProgessDialog()
        RestClient().getInstance().getRestService().updateGoodsIssueRequest(id, removeReq)
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

    private fun confirm() {

        if (status == 1) {
            showToast("Trạng thái đã hoàn thành")
            return
        }

        showProgessDialog()
        RestClient().getInstance().getRestService().confirmGoodsIssueDocument(id)
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

    private lateinit var date: Calendar
    private fun showDateTimePicker() {
        val currentDate: Calendar = Calendar.getInstance()
        date = Calendar.getInstance()
        DatePickerDialog(
            this,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    this,
                    OnTimeSetListener { view, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        val format =
                            SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")

                        val formatToShow =
                            SimpleDateFormat("dd/MM/yyyy hh:mm a")

                        val dateTime = format.format(date.time).toString()
                        val dateTimeToShow = formatToShow.format(date.time).toString()
                        plannedDatetime = dateTime
                        updatePlanDate()
                    },
                    currentDate.get(Calendar.HOUR_OF_DAY),
                    currentDate.get(Calendar.MINUTE),
                    false
                ).show()
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DATE)
        ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 100) {
            getData(id)
        }

        if (resultCode == 101) {
            addProduct()
            Log.e("hailpt~~", " line > " + ConstantsApp.lines.size)
            ConstantsApp.lines.clear()

        }
    }
}
