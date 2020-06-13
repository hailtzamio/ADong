package com.zamio.adong.ui.ware.stock.stock

import RestClient
import WareHouseAdapter
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.WareHouse
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_stock_list.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StockListActivity : BaseActivity() {

    var mList = ArrayList<WareHouse>()
    var isChooseStock = false // CreateGoodsReceivedNoteActivity
    var type = "STOCK"
    override fun getLayout(): Int {
        return R.layout.activity_stock_list
    }

    override fun initView() {
        rightButton.visibility = View.GONE
        rightButton.setOnClickListener {
            val intent = Intent(this, CreateStockActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_STATUS, type)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun initData() {
        if(intent.hasExtra(ConstantsApp.KEY_VALUES_STATUS)) {
            type = intent.getStringExtra(ConstantsApp.KEY_VALUES_STATUS)!!
            if(type == "FACTORY") {
                tvTitle.text = "Danh Sách Xưởng"
            } else {
                tvTitle.text = "Danh Sách Kho"
            }
        }
    }

    override fun resumeData() {
        mList.clear()
        getData()
    }

    private fun getData() {
        showProgessDialog()
        RestClient().getInstance().getRestService()
            .getStocks("",type)
            .enqueue(object :
                Callback<RestData<ArrayList<WareHouse>>> {
                override fun onFailure(call: Call<RestData<ArrayList<WareHouse>>>?, t: Throwable?) {
                    dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<ArrayList<WareHouse>>>?,
                    response: Response<RestData<ArrayList<WareHouse>>>?
                ) {
                    dismisProgressDialog()
                    if (response!!.body() != null && response.body().status == 1) {
                        mList = response.body().data!!
                        setupRecyclerVieww()

                    }
                }
            })

        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            isChooseStock = true
        }
    }

    private fun setupRecyclerVieww() {
        if (recyclerView != null) {
            val mAdapter = WareHouseAdapter(mList)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(false)
            recyclerView.adapter = mAdapter

            mAdapter.onItemClick = { product ->


                if (isChooseStock) {
                    val dialogClickListener =
                        DialogInterface.OnClickListener { dialog, which ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    val returnIntent = Intent()
                                    returnIntent.putExtra("warehouseId", product.id)
                                    returnIntent.putExtra("warehouseName", product.name)
                                    setResult(100, returnIntent)
                                    finish()
                                }
                                DialogInterface.BUTTON_NEGATIVE -> {
                                }
                            }
                        }

                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setMessage("Chọn "+product.name + " ?").setPositiveButton("Đồng ý", dialogClickListener)
                        .setNegativeButton("Không", dialogClickListener).show()
                } else {
                    val intent = Intent(this, DetailStockActivity::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.id)
                    intent.putExtra(ConstantsApp.KEY_VALUES_STATUS, product.keeperId)
                    startActivityForResult(intent, 1000)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }

        }
    }
}
