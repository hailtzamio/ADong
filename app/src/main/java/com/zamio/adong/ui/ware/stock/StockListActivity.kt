package com.zamio.adong.ui.ware.stock

import RestClient
import WareHouseAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.WareHouse
import kotlinx.android.synthetic.main.activity_stock_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StockListActivity : BaseActivity() {

    var mList: List<WareHouse>? = null
    override fun getLayout(): Int {
       return R.layout.activity_stock_list
    }

    override fun initView() {

    }

    override fun initData() {
        getData()
    }

    override fun resumeData() {

    }


    private fun getData() {
        showProgessDialog()
        RestClient().getInstance().getRestService()
            .getStocks("")
            .enqueue(object :
                Callback<RestData<List<WareHouse>>> {
                override fun onFailure(call: Call<RestData<List<WareHouse>>>?, t: Throwable?) {
                dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<List<WareHouse>>>?,
                    response: Response<RestData<List<WareHouse>>>?
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
            val mAdapter = WareHouseAdapter(mList!!)
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
