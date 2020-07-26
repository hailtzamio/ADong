package com.zamio.adong.ui.ware.stock.goods_issue_request

import GoodsIssueRequestAdapter
import RestClient
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.adapter.PaginationScrollListener
import com.zamio.adong.model.GoodsIssueRequest
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_stock_list.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoodsIssueRequestListActivity : BaseActivity() {

    var mList = ArrayList<GoodsIssueRequest>()
    var id = 0
    override fun getLayout(): Int {
        return R.layout.activity_stock_list
    }

    override fun initView() {
        tvTitle.text = "Yêu Cầu Xuất Kho"
        rightButton.visibility = View.GONE
        rightButton.setOnClickListener {
            val intent = Intent(this, CreateGoodsIssueRequestActivity::class.java)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun initData() {
        id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 0)
    }

    override fun resumeData() {
        resetData()
        getData()
    }

    private fun resetData() {
        page = 0
        mList.clear()
    }

    private fun getData() {
        showProgessDialog()
        RestClient().getInstance().getRestService()
            .getGoodsIssueRequests(page, id)
            .enqueue(object :
                Callback<RestData<ArrayList<GoodsIssueRequest>>> {
                override fun onFailure(call: Call<RestData<ArrayList<GoodsIssueRequest>>>?, t: Throwable?) {
                    dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<ArrayList<GoodsIssueRequest>>>?,
                    response: Response<RestData<ArrayList<GoodsIssueRequest>>>?
                ) {
                    dismisProgressDialog()
                    if (response!!.body() != null && response.body().status == 1) {
                        mList.addAll(response.body().data!!)
                        totalPages = response.body().pagination!!.totalPages!!
                        mAdapter.notifyDataSetChanged()
                        page += 1
                        setupRecyclerVieww()

                        if(mList.size == 0) {
                            showToast("Danh sách trống !")
                        }
                    }
                }
            })
    }


    val mAdapter = GoodsIssueRequestAdapter(mList)
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var currentPage = 0
    var totalPages = 0
    var page = 0
    private fun setupRecyclerVieww() {
        if (recyclerView != null) {
            val linearLayoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.setHasFixedSize(false)
            recyclerView.adapter = mAdapter

            mAdapter.onItemClick = { product ->
                val intent = Intent(this, DetailGoodsIssueRequestActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.id)
                startActivityForResult(intent, 1000)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            recyclerView?.addOnScrollListener(object :
                PaginationScrollListener(linearLayoutManager) {
                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }

                override fun loadMoreItems() {
                    isLoading = true
                    getMoreItems()
                }
            })

        }
    }

    fun getMoreItems() {
        isLoading = false
        if (page < totalPages) {
            getData()
        }
    }
}
