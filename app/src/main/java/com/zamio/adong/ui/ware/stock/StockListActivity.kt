package com.zamio.adong.ui.ware.stock

import RestClient
import WareHouseAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.WareHouse
import kotlinx.android.synthetic.main.activity_stock_list.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StockListActivity : BaseActivity() {

    var mList = ArrayList<WareHouse>()
    override fun getLayout(): Int {
        return R.layout.activity_stock_list
    }

    override fun initView() {
        rightButton.setOnClickListener {
            val intent = Intent(this, CreateStockActivity::class.java)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun initData() {

    }

    override fun resumeData() {
        mList.clear()
        getData()
    }

    private fun getData() {
        showProgessDialog()
        RestClient().getInstance().getRestService()
            .getStocks("")
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
    }

    private fun setupRecyclerVieww() {
        if (recyclerView != null) {
            val mAdapter = WareHouseAdapter(mList)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(false)
            recyclerView.adapter = mAdapter

            mAdapter.onItemClick = { product ->
                //                val intent = Intent(context, DetailLorryActivity::class.java)
////            intent.putExtra(ConstantsApp.KEY_PERMISSION, actionString)
//                intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.id)
//                startActivityForResult(intent,1000)
//                activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

        }
    }
}
