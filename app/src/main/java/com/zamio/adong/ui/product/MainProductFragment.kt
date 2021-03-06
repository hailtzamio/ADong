package com.zamio.adong.ui.product

import ProductAdapter
import RestClient
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.adapter.PaginationScrollListener
import com.zamio.adong.model.Product
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.network.Pagination
import kotlinx.android.synthetic.main.activity_checkout_in_worker_list.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.recyclerView
import kotlinx.android.synthetic.main.fragment_home.viewNoData
import kotlinx.android.synthetic.main.item_header_layout.*
import kotlinx.android.synthetic.main.item_search_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainProductFragment : BaseFragment() {

    var currentPage = 0
    var totalPages = 0
    var products:List<Product>? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getProducts(0)
        rightButton.setOnClickListener {
            val intent = Intent(context, CreateProductActivity::class.java)
            intent.putExtra("EMAIL", "")
            startActivityForResult(intent,1000)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        imvBack.setOnClickListener {
            activity!!.onBackPressed()
        }

        if(activity is MainProductActivity){
            if(!ConstantsApp.PERMISSION!!.contains("c")){
                rightButton.visibility = View.GONE
            }
        }

        edtSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getProducts(0)
                return@OnEditorActionListener true
            }
            false
        })

        imvSearch.setOnClickListener {
            getProducts(0)
        }
    }

    override fun onResume() {
        super.onResume()
        getProducts(0)
    }

    private fun getProducts(page:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getProducts(page,edtSearch.text.toString()).enqueue(object :
            Callback<RestData<ArrayList<Product>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Product>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<Product>>>?, response: Response<RestData<ArrayList<Product>>>?) {
                    dismisProgressDialog()
                    if(response!!.body() != null && response.body().status == 1){
                        products = response.body().data!!
                        setupRecyclerView()
                        totalPages = response.body().pagination!!.totalPages!!

                        val pagination = response.body().pagination!!
                        if (pagination.totalRecords != null) {
                            showHintText(pagination)
                        }

                        if (products!!.isNotEmpty()) {
                            viewNoData.visibility = View.GONE
                        } else {
                            viewNoData.visibility = View.VISIBLE
                        }
                    }
            }
        })
    }

    private fun showHintText(pagination: Pagination) {

        if (pagination.totalRecords != null) {

            var count = pagination.totalRecords!!.toString()

            if (pagination.totalRecords!! > 1000) {
                count = "1000+"
            }

            if (pagination.totalRecords!! > 2000) {
                count = "2000+"
            }

            if (pagination.totalRecords!! > 3000) {
                count = "3000+"
            }

            edtSearch.hint = "Tìm kiếm trong $count vật tư"
        }
    }

    private fun setupRecyclerView(){

        val mAdapter = ProductAdapter(products!!,false)
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            val intent = Intent(context, DetailProductActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.id)
            startActivityForResult(intent,1000)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        var isLastPage: Boolean = false
        var isLoading: Boolean = false

        recyclerView?.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager ) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
//                if((currentPage + 1) < totalPages){
//                    getProducts(currentPage++)
//                }
//                currentPage += 1
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 100){
//            getProducts(0)
        }
    }
}
