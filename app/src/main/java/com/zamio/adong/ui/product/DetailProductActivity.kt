package com.zamio.adong.ui.product

import InformationAdapter
import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.model.Information
import com.zamio.adong.model.Product
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.activity.PreviewImageActivity
import kotlinx.android.synthetic.main.activity_detail_driver.*
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.activity_detail_product.cropImageView
import kotlinx.android.synthetic.main.activity_detail_product.recyclerView
import kotlinx.android.synthetic.main.activity_detail_product.tvOk
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProductActivity : BaseActivity() {

    var model:Product? = null
    var productId = 1
    val mList = ArrayList<Information>()
    override fun getLayout(): Int {
        return R.layout.activity_detail_product
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)){

            productId = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)


            if(!ConstantsApp.PERMISSION.contains("u")){
                rightButton.visibility = View.GONE
            }

            if(!ConstantsApp.PERMISSION.contains("d")){
                tvOk.visibility = View.GONE
            }

            rightButton.setOnClickListener {
                val intent = Intent(this, UpdateProductActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, model!!)
                startActivityForResult(intent, 1000)
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
                builder.setMessage("Xóa vật tư này?").setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show()
            }

            cropImageView.setOnClickListener {
                if(model!!.thumbnailUrl != null) {
                    val intent = Intent(this, PreviewImageActivity::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_ID, model!!.thumbnailUrl)
                    startActivityForResult(intent, 1000)
                }
            }

            if (intent.hasExtra(ConstantsApp.KEY_VALUES_HIDE)){
                tvOk.visibility = View.GONE
                rightButton.visibility = View.GONE
            }
        }


    }

    override fun resumeData() {
        getProduct(productId)
    }

    private fun getProduct(id:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getProduct(id).enqueue(object :
            Callback<RestData<Product>> {

            override fun onFailure(call: Call<RestData<Product>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<Product>>?, response: Response<RestData<Product>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response!!.body().status == 1){
                    model = response.body().data ?: return
                    mList.clear()
                    var type = "Mua tại công trình"
                    when (model!!.type) {
                        "buy" -> type= "Mua tại công trình"
                        "manufacture" ->  type = "Sản xuất"
                        "tool" ->  type = "Công cụ"
                    }

                    mList.add(Information("Tên",model!!.name ?: "---", ""))
                    mList.add(Information("Mã",model!!.code ?: "---", ""))
                    mList.add(Information("Loại",type, ""))
                    mList.add(Information("Đơn vị tính",model!!.unit ?: "---", ""))
                    mList.add(Information("Chiêu dài",model!!.length.toString() ?: "---", ""))
                    mList.add(Information("Chiều rộng",model!!.width.toString() ?: "---", ""))
                    mList.add(Information("Chiều cao",model!!.height.toString() ?: "---", ""))
                    mList.add(Information("Cân nặng",model!!.weight.toString() ?: "---", ""))


                    Picasso.get().load(model!!.thumbnailUrl).into(cropImageView)

                    setupRecyclerView(mList)
                }
            }
        })
    }

    private fun setupRecyclerView(data: List<Information>) {
        val mAdapter = InformationAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter
    }

    private fun removeLorry(){
        showProgessDialog()
        RestClient().getRestService().removeProduct(model!!.id).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response.body().status == 1){
                    showToast("Xóa thành công")
                    setResult(100)
                    finish()
                } else {
                    showToast("Không thành công")
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 100){
            getProduct(productId)
        }
    }
}
