package com.zamio.adong.ui.project.tab.ui.main.requirement

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

class DetailProductRequrementActivity : BaseActivity() {

    var data = ArrayList<Product>()
    var projectChoose = ArrayList<Product>()
    var productRequirement: ProductRequirement? = null
    var warehouseId = 0
    var warehouseName = ""
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
                        val intent = Intent(this, StockListActivity::class.java)
                        intent.putExtra(ConstantsApp.KEY_VALUES_ID, 0)
                        startActivityForResult(intent, 1000)
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {

                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Chọn Kho nhận phiếu yêu cầu?")
            .setPositiveButton("Đồng ý", dialogClickListener)
            .setNegativeButton("Không", dialogClickListener).show()
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {

            productRequirement =
                intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as ProductRequirement

            if (productRequirement != null) {
                data = productRequirement!!.lines
                tvName.text = productRequirement!!.projectName
                if(productRequirement!!.note != null && productRequirement!!.note != "") {
                    tvNote.text = productRequirement!!.note
                } else {
                    rlNote.visibility = View.GONE
                }

                tvDate.text = productRequirement!!.expectedDatetime
                setupRecyclerView()
            }
        }
    }

    private fun doCreate() {
        val goodsNoteUpdateRq = GoodsNoteUpdateRq2("")
        goodsNoteUpdateRq.plannedDatetime = productRequirement!!.expectedDatetime

        if(productRequirement!!.note != null) {
            goodsNoteUpdateRq.note = productRequirement!!.note!!
        }

        goodsNoteUpdateRq.warehouseId = warehouseId
        goodsNoteUpdateRq.productReqId = productRequirement!!.id

        projectChoose.forEach {
            val linesAddNew = IssueRequestLine(it.id)
            goodsNoteUpdateRq.linesAddNew.add(linesAddNew)
        }

        create(goodsNoteUpdateRq)
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

        val mAdapter = ProductRequirementDetailAdapter(data!!)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            val intent = Intent(this, DetailProductActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.productId)
            intent.putExtra(ConstantsApp.KEY_VALUES_HIDE, product.productId)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        mAdapter.onItemSelected = { position, isChecked ->
            data[position].isSelected = isChecked

            if (isChecked) {
                projectChoose.add(data[position])
            } else {
                for (i in projectChoose.indices) {
                    for (j in (projectChoose.size - 1) downTo 0) {
                        if (data[position].id == projectChoose[j].id) {
                            projectChoose.removeAt(j)
                        }
                    }
                }
            }

            if(projectChoose.size > 0) {
                rightButton.isEnabled = true
                rightButton.setTextColor(Color.parseColor("#ffffff"));
            } else {
                rightButton.isEnabled = false
                rightButton.setTextColor(Color.parseColor("#90928E"));
            }

        }
    }

    override fun resumeData() {

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 100) {
            warehouseId = data!!.getIntExtra("warehouseId", 0)
            warehouseName = data.getStringExtra("warehouseName")!!
            doCreate()
        }
    }
}
