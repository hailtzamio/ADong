package com.zamio.adong.ui.product

import ProductAdapter
import RestClient
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Product
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_list_product.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoveActivity : BaseActivity() {

    var actionString = ""
    override fun getLayout(): Int {
       return R.layout.activity_list_product
    }

    override fun initView() {

    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_PERMISSION)){
            actionString = intent.getStringExtra(ConstantsApp.KEY_PERMISSION)

            if(!actionString.contains("c")){
               rightButton.visibility = View.GONE
            }
        }
        getProducts()
    }

    override fun resumeData() {

    }

    private fun getProducts(){
        showProgessDialog()
        RestClient().getInstance().getRestService().getProducts(0).enqueue(object :
            Callback<RestData<List<Product>>> {
            override fun onFailure(call: Call<RestData<List<Product>>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<RestData<List<Product>>>?, response: Response<RestData<List<Product>>>?) {
                dismisProgressDialog()
                if( response!!.body().status == 1){
                    setupRecyclerView(response.body().data!!)
                }
            }
        })
    }

    private fun setupRecyclerView(data:List<Product>){
        val mAdapter = ProductAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            val intent = Intent(this, DetailProductActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_PERMISSION, actionString)
            intent.putExtra(ConstantsApp.KEY_QUESTION_ID, product.id)
            startActivity(intent)
            this!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}
