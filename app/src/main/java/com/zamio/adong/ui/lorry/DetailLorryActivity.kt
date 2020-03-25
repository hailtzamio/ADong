package com.zamio.adong.ui.lorry

import RestClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Lorry
import com.zamio.adong.model.Product
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_detail_lorry.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailLorryActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_detail_lorry
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết Xe"
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_QUESTION_ID)){

            val productId = intent.getIntExtra(ConstantsApp.KEY_QUESTION_ID, 1)

            if(!ConstantsApp.PERMISSION.contains("u")){
                rightButton.visibility = View.GONE
            }

            getLorry(productId)
        }
    }

    override fun resumeData() {

    }

    private fun getLorry(id:Int){
        showProgessDialog()
        RestClient().getRestService().getLorry(id).enqueue(object :
            Callback<RestData<Lorry>> {

            override fun onFailure(call: Call<RestData<Lorry>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<Lorry>>?, response: Response<RestData<Lorry>>?) {
                dismisProgressDialog()
                if( response!!.body().status == 1){
                    val product = response.body().data ?: return
                    tvName.text = product.capacity
                    tvType.text = product.plateNumber
                    tvUnit.text = product.brand
                    tvQuantity.text = product.model
                }
            }
        })
    }

}
