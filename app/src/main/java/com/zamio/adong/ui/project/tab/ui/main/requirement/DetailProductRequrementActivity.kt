package com.zamio.adong.ui.project.tab.ui.main.requirement

import ProductRequirementDetailAdapter
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import com.zamio.adong.model.Product
import com.zamio.adong.model.ProductRequirement
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.product.DetailProductActivity
import kotlinx.android.synthetic.main.activity_detail_product_requirement.*
import kotlinx.android.synthetic.main.item_header_layout.*

class DetailProductRequrementActivity : BaseActivity() {

    var data:List<Product>? = null
    override fun getLayout(): Int {
        return R.layout.activity_detail_product_requirement
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.visibility = View.GONE
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)){

            val productOb = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as ProductRequirement
            data = productOb.lines
            tvName.text = productOb.projectName
            tvNote.text = productOb.note
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView(){

        val mAdapter = ProductRequirementDetailAdapter(data!!)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            val intent = Intent(this, DetailProductActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.productId)
            intent.putExtra(ConstantsApp.KEY_VALUES_HIDE, product.productId)
            startActivityForResult(intent,1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun resumeData() {

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
