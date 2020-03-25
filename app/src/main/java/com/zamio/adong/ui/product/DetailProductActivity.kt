package com.zamio.adong.ui.product

import RestClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Product
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProductActivity : BaseActivity() {


    override fun getLayout(): Int {
        return R.layout.activity_detail_product
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_QUESTION_ID)){

            val productId = intent.getIntExtra(ConstantsApp.KEY_QUESTION_ID, 1)
            val actionString = intent.getStringExtra(ConstantsApp.KEY_PERMISSION)

            if( actionString != null && !actionString.contains("u")){
                rightButton.visibility = View.GONE
            }

            getProduct(productId)
        }
    }

    override fun resumeData() {

    }

    private fun getProduct(id:Int){
        showProgessDialog()
        RestClient().getRestService().getProduct(id).enqueue(object :
            Callback<RestData<Product>> {

            override fun onFailure(call: Call<RestData<Product>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<Product>>?, response: Response<RestData<Product>>?) {
                dismisProgressDialog()
                if( response!!.body().status == 1){
                    val product = response.body().data ?: return
                    tvName.text = product.name
                    tvType.text = product.type
                    tvUnit.text = product.unit
                    tvQuantity.text = product.quantity.toString()
                }
            }
        })
    }

}
