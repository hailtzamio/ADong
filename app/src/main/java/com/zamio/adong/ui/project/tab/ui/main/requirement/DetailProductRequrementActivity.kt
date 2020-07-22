package com.zamio.adong.ui.project.tab.ui.main.requirement

import InformationAdapter
import ProductRequirementDetailAdapter
import RestClient
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.*
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.product.DetailProductActivity
import com.zamio.adong.ui.ware.stock.stock.StockListActivity
import kotlinx.android.synthetic.main.activity_detail_product_requirement.*
import kotlinx.android.synthetic.main.activity_detail_product_requirement.recyclerView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.graphics.Color
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.zamio.adong.ui.project.ChooseManagerActivity
import com.zamio.adong.utils.Utils
import kotlinx.android.synthetic.main.activity_detail_team.*
import kotlin.collections.ArrayList

class DetailProductRequrementActivity : BaseActivity() {

    var data = ArrayList<Product>()
    var projectChoose = ArrayList<Product>()
    var productRequirement: ProductRequirement? = null
    var warehouseId = 0
    var assigneeId = 0
    var warehouseName = ""
    var title = "Chọn người đi mua?"
    val mList = ArrayList<Information>()
    override fun getLayout(): Int {
        return R.layout.activity_detail_product_requirement
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setOnClickListener {
            val dialogClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            showDateTimePicker()
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                            plannedStartDate = productRequirement!!.expectedDatetime
                            askToGoToStockList()
                        }
                    }
                }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Chọn thời gian ?")
                .setPositiveButton("Đồng ý", dialogClickListener)
                .setNegativeButton("Không", dialogClickListener).show()
        }
    }


    var type = 0
    private fun setupChooseSpinner() {
        val list: MutableList<String> = ArrayList()
        list.add("Mua")
        list.add("Xưởng")
        list.add("Kho")

        val dataAdapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item, list
        )
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinType.adapter = dataAdapter
        spinType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                type = position
                when (position) {
                    0 -> title = "Chọn người đi mua?"
                    1 -> title = "Chọn xưởng sản xuất?"
                    2 -> title = "Chọn kho?"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private lateinit var date: Calendar
    var plannedStartDate = ""
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
                            SimpleDateFormat("hh:mm a dd/MM/yyyy")

                        val dateTime = format.format(date.time).toString()
                        val dateTimeToShow = formatToShow.format(date.time).toString()
                        askToGoToStockList()
                        plannedStartDate = dateTime
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

    private fun askToGoToStockList() {

        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (type == 0) {
                            chooseWorker()
                        } else {
                            chooseStock()
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

    private fun chooseStock() {
        val intent = Intent(this, StockListActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID, 0)
        if (type == 1) {
            intent.putExtra(ConstantsApp.KEY_VALUES_STATUS, "FACTORY")
        } else {
            intent.putExtra(ConstantsApp.KEY_VALUES_STATUS, "STOCK")
        }

        startActivityForResult(intent, 1000)
    }

    private fun chooseWorker() {
        val intent = Intent(this, ChooseManagerActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID, 101)
        startActivityForResult(intent, 1000)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {

            productRequirement =
                intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as ProductRequirement

            if (productRequirement != null) {
                data = productRequirement!!.lines

                mList.add(Information("Tên dự án", productRequirement!!.projectName ?: "---", ""))
                mList.add(
                    Information(
                        "Ngày dự kiến",
                        Utils.convertDate(productRequirement!!.expectedDatetime) ?: "---",
                        ""
                    )
                )

                if(productRequirement!!.note == "") {
                    productRequirement!!.note = null
                }

                mList.add(Information("Ghi chú", productRequirement!!.note ?: "---", ""))


                setupRecyclerView()
            }

            setupChooseSpinner()

            setupRecyclerViewTop(mList)
        }
    }

    private fun setupRecyclerViewTop(data: List<Information>) {
        val mAdapter = InformationAdapter(data)
        recyclerView2.layoutManager = LinearLayoutManager(this)
        recyclerView2.setHasFixedSize(false)
        recyclerView2.adapter = mAdapter
    }

    private fun doCreate() {
        val goodsNoteUpdateRq = GoodsNoteUpdateRq2("")
        goodsNoteUpdateRq.plannedDatetime = productRequirement!!.expectedDatetime

        if (productRequirement!!.note != null) {
            goodsNoteUpdateRq.note = productRequirement!!.note!!
        }

        goodsNoteUpdateRq.warehouseId = warehouseId
        goodsNoteUpdateRq.productReqId = productRequirement!!.id

        projectChoose.forEach {
            val linesAddNew = IssueRequestLine(it.id, null, null)
            goodsNoteUpdateRq.linesAddNew.add(linesAddNew)
        }

        create(goodsNoteUpdateRq)
    }

    private fun doBuy() {
        val goodsNoteUpdateRq = GoodsNoteUpdateRq2("")
        goodsNoteUpdateRq.plannedDatetime = productRequirement!!.expectedDatetime

        if (productRequirement!!.note != null) {
            goodsNoteUpdateRq.note = productRequirement!!.note!!
        }

        goodsNoteUpdateRq.assigneeId = assigneeId
        goodsNoteUpdateRq.productRequirementId = productRequirement!!.id

        projectChoose.forEach {
            val linesAddNew = IssueRequestLine(null, null, it.id)
            goodsNoteUpdateRq.linesAddNew.add(linesAddNew)
        }

        showProgessDialog()
        RestClient().getInstance().getRestService().createPurchaseRequest(goodsNoteUpdateRq)
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
                    if (response!!.body() != null && response!!.body().status == 1) {
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

    private fun doManufactureRequest() {

        val goodsNoteUpdateRq = GoodsNoteUpdateRq2("")
        goodsNoteUpdateRq.plannedDatetime = productRequirement!!.expectedDatetime

        if (productRequirement!!.note != null) {
            goodsNoteUpdateRq.note = productRequirement!!.note!!
        }

        goodsNoteUpdateRq.warehouseId = warehouseId
        goodsNoteUpdateRq.productRequirementId = productRequirement!!.id

        projectChoose.forEach {
            val linesAddNew = IssueRequestLine(null, null, it.id)
            goodsNoteUpdateRq.linesAddNew.add(linesAddNew)
        }

        showProgessDialog()
        RestClient().getInstance().getRestService().createManufactureRequest(goodsNoteUpdateRq)
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
                    if (response!!.body() != null && response!!.body().status == 1) {
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

    private fun create(dataOb: GoodsNoteUpdateRq2) {
        showProgessDialog()
        RestClient().getInstance().getRestService().createGoodsIssueRequest(dataOb).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
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

    private fun setupRecyclerView() {

        val data2 = ArrayList<Information>()

        data.forEach {
            if(it.note != null) {
                data2.add(Information(it.quantity.toString() + " ${it.productUnit}" + " \n" + it.note ?: "", it.productName, ""))
            } else {
                data2.add(Information(it.quantity.toString() + " ${it.productUnit}", it.productName, ""))
            }

        }

        val mAdapter = InformationAdapter(data2)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
//            val intent = Intent(this, DetailProductActivity::class.java)
//            intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.productId)
//            intent.putExtra(ConstantsApp.KEY_VALUES_HIDE, product.productId)
//            startActivityForResult(intent, 1000)
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

//        mAdapter.onItemSelected = { position, isChecked ->
//            data[position].isSelected = isChecked
//
//            if (isChecked) {
//                projectChoose.add(data[position])
//            } else {
//                for (i in projectChoose.indices) {
//                    for (j in (projectChoose.size - 1) downTo 0) {
//                        if (data[position].id == projectChoose[j].id) {
//                            projectChoose.removeAt(j)
//                        }
//                    }
//                }
//            }
//
//            if (projectChoose.size > 0) {
//                rightButton.isEnabled = true
//                rightButton.setTextColor(Color.parseColor("#ffffff"));
//            } else {
//                rightButton.isEnabled = false
//                rightButton.setTextColor(Color.parseColor("#90928E"));
//            }
//
//        }
    }

    override fun resumeData() {

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 100) {
            warehouseId = data!!.getIntExtra("warehouseId", 0)
            warehouseName = data.getStringExtra("warehouseName")!!
            when (type) {
                1 -> doManufactureRequest()
                2 -> doCreate()
            }
        }

        if (resultCode == 101) {
            assigneeId = data!!.getIntExtra("id", 0)
            doBuy()
        }

    }
}
