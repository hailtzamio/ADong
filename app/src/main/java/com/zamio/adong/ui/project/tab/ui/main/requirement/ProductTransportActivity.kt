package com.zamio.adong.ui.project.tab.ui.main.requirement

import ProductTransportAdapter
import RestClient
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.ProductRequirement
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_product_transport.*
import kotlinx.android.synthetic.main.activity_product_transport.tvTitle
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductTransportActivity : BaseActivity() {


    var data:ArrayList<ProductRequirement>? = null
    var id = 0
    override fun getLayout(): Int {
       return R.layout.activity_product_transport
    }

    override fun initView() {
        tvTitle.text = "Vận Chuyển"
    }

    override fun initData() {

        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            id =  intent.extras!!.getInt(ConstantsApp.KEY_VALUES_ID)
            getData()
            getData2()
        }
    }

    override fun resumeData() {

    }

    fun getData(){
        showProgessDialog()
        RestClient().getInstance().getRestService().getProductRequirementGoodsIssue(id,id).enqueue(object :
            Callback<RestData<ArrayList<ProductRequirement>>> {
            override fun onFailure(call: Call<RestData<ArrayList<ProductRequirement>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<ProductRequirement>>>?, response: Response<RestData<ArrayList<ProductRequirement>>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response.body().status == 1){
                    data = response.body().data!!
                    if(data != null && data!!.size > 0) {
                        tv1.visibility = View.VISIBLE
                        setupRecyclerView()
                    }

                }
            }
        })
    }

    fun getData2(){
        showProgessDialog()
        RestClient().getInstance().getRestService().getProductRequirementpurchaseRequests(id,id).enqueue(object :
            Callback<RestData<ArrayList<ProductRequirement>>> {
            override fun onFailure(call: Call<RestData<ArrayList<ProductRequirement>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<ProductRequirement>>>?, response: Response<RestData<ArrayList<ProductRequirement>>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response.body().status == 1){
                    data = response.body().data!!
                    if(data != null && data!!.size > 0) {
                        tv2.visibility = View.VISIBLE
                        setupRecyclerView2()
                    }
                }
            }
        })
    }

    private fun setupRecyclerView(){
        if ( recyclerView != null ) {
            val mAdapter = ProductTransportAdapter(data!!)
            val linearLayoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.setHasFixedSize(false)
            recyclerView.adapter = mAdapter

            mAdapter.onItemClick = { product ->
                val intent = Intent(this, ProductTransportDetailActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, product)
                startActivityForResult(intent,1000)
               overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    private fun setupRecyclerView2(){
        if ( recyclerView2 != null ) {
            val mAdapter = ProductTransportAdapter(data!!)
            val linearLayoutManager = LinearLayoutManager(this)
            recyclerView2.layoutManager = linearLayoutManager
            recyclerView2.setHasFixedSize(false)
            recyclerView2.adapter = mAdapter

            mAdapter.onItemClick = { product ->
                val intent = Intent(this, ProductTransportDetailActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, product)
                startActivityForResult(intent,1000)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

}