package com.zamio.adong.ui.product

import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.Product
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.lorry.UpdateLorryActivity
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProductActivity : BaseActivity() {

    var product:Product? = null
    var productId = 1
    override fun getLayout(): Int {
        return R.layout.activity_detail_product
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_QUESTION_ID)){

            productId = intent.getIntExtra(ConstantsApp.KEY_QUESTION_ID, 1)


            if(!ConstantsApp.PERMISSION.contains("u")){
                rightButton.visibility = View.GONE
            }

            if(!ConstantsApp.PERMISSION.contains("d")){
                tvOk.visibility = View.GONE
            }



            rightButton.setOnClickListener {
                val intent = Intent(this, UpdateProductActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_QUESTION_ID, product!!)
                startActivity(intent)
                this!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            tvOk.setOnClickListener {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                removeLorry()
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage("xóa vật tư này?").setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show()
            }
        }
    }

    override fun resumeData() {
        getProduct(productId)
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
                    product = response.body().data ?: return
                    tvName.text = product!!.name
                    tvType.text = product!!.type
                    tvUnit.text = product!!.unit
                    tvQuantity.text = product!!.quantity.toString()
                }
            }
        })
    }

    private fun removeLorry(){
        showProgessDialog()
        RestClient().getRestService().removeProduct(product!!.id).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if( response!!.body().status == 1){
                    showToast("Xóa thành công")
                    onBackPressed()
                }
            }
        })
    }

}
