package com.zamio.adong.ui.ware.stock.goods_received

import ProductAdapter
import RestClient
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.LinesAddNew
import com.zamio.adong.model.Product
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.product.DetailProductActivity
import kotlinx.android.synthetic.main.activity_create_goods_received.*
import kotlinx.android.synthetic.main.item_header_layout.*
import kotlinx.android.synthetic.main.item_search_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class AddProductToGoodsReceiedActivity : BaseActivity() {

    var currentPage = 0
    var totalPages = 0
    var products = ArrayList<Product>()
    var productChoose = ArrayList<Product>()
    var id = 0
    override fun getLayout(): Int {
        return R.layout.activity_create_product_goods_receied
    }

    override fun initView() {
        rightButton.setImageResource(R.drawable.tick)
    }

    override fun initData() {

        id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 0)

        getProducts(0)
        imvBack.setOnClickListener {
            onBackPressed()
        }


        if (!ConstantsApp.PERMISSION!!.contains("c")) {
            rightButton.visibility = View.GONE
        }

        edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getProducts(0)
                return@OnEditorActionListener true
            }
            false
        })

        imvSearch.setOnClickListener {
            getProducts(0)
        }

        tvTitle.text = "Chọn Vật Tư"
        rightButton.setOnClickListener {

            for (j in (products.size - 1) downTo 0) {
                if (products[j].quantityChoose != 0) {
                    Log.e("hailpt~~", " Come ")
                    productChoose.add(products[j])
                }
            }

            Log.e("hailpt~~", " before " + productChoose.size)

            var count = productChoose.size
            for (i in 0 until count) {
                var j = i + 1
                while (j < count) {
                    if (productChoose[i].id == productChoose[j].id) {
                        productChoose.removeAt(j--)
                        count--
                    }
                    j++
                }
            }

            Log.e("hailpt~~", " after " + productChoose.size)
            ConstantsApp.productsToGooodReceied = productChoose
            productChoose.forEach {
                val line = LinesAddNew(0,0,it.id, it.quantityChoose, "", null,"","")
                ConstantsApp.lines.add(line)
            }
            Log.e("hailpt~~", " line " + ConstantsApp.lines.size)
            setResult(101)
            finish()
        }
    }

    override fun resumeData() {

    }

    override fun onResume() {
        super.onResume()
    }

    private fun getProducts(page: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getProducts(page, edtSearch.text.toString())
            .enqueue(object :
                Callback<RestData<ArrayList<Product>>> {
                override fun onFailure(call: Call<RestData<ArrayList<Product>>>?, t: Throwable?) {
                    dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<ArrayList<Product>>>?,
                    response: Response<RestData<ArrayList<Product>>>?
                ) {
                    dismisProgressDialog()
                    if (response!!.body().status == 1) {
                        val productRes = response.body().data!!


                        totalPages = response.body().pagination!!.totalPages!!

                        for (j in (products.size - 1) downTo 0) {
                            if (products[j].quantityChoose != 0) {
                                productChoose.add(products[j])
                            }
                        }

                        for (i in productChoose.indices) {
                            for (j in (productRes.size - 1) downTo 0) {
                                if (productChoose[i].id == productRes[j].id) {
                                    productRes[j].quantityChoose = productChoose[i].quantityChoose
                                }
                            }
                        }

                        products = productRes

                        setupRecyclerView()
                    }
                }
            })
    }

    private fun setupRecyclerView() {
        val mAdapter = ProductAdapter(products, true)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            val intent = Intent(this, DetailProductActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.id)
            intent.putExtra(ConstantsApp.KEY_VALUES_HIDE, product.id)
            intent.putExtra(ConstantsApp.ChooseTeamWorkerActivity, "")
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        mAdapter.onItemSelected = { position, isChecked ->
            //            workers[position].isSelected = isChecked
//
//            if (isChecked) {
//                workersChoose.add(workers[position])
//            } else {
//                for (i in workersChoose.indices) {
//                    for (j in (workersChoose.size - 1) downTo 0) {
//                        if (workers[position].id == workersChoose[j].id) {
//                            workersChoose.removeAt(j)
//                        }
//                    }
//                }
//            }
        }
    }
}
