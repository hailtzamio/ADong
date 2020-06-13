package com.zamio.adong.ui.ware.stock.goods_issue

import GoodsIssueAdapterr
import GoodsReceivedNoteAdapter
import RestClient
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.adapter.PaginationScrollListener
import com.zamio.adong.model.GoodsIssue
import com.zamio.adong.model.GoodsNote
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_stock_list.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoodsIssueListActivity : BaseActivity() {

    var mList = ArrayList<GoodsIssue>()
    override fun getLayout(): Int {
        return R.layout.activity_stock_list
    }

    override fun initView() {
        tvTitle.text = "Phiếu Xuất Kho"
        rightButton.visibility = View.GONE
        rightButton.setOnClickListener {
            val intent = Intent(this, CreateGoodsIssueActivity::class.java)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun initData() {

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
            .getGoodsIssues(page)
            .enqueue(object :
                Callback<RestData<ArrayList<GoodsIssue>>> {
                override fun onFailure(call: Call<RestData<ArrayList<GoodsIssue>>>?, t: Throwable?) {
                    dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<ArrayList<GoodsIssue>>>?,
                    response: Response<RestData<ArrayList<GoodsIssue>>>?
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


    val mAdapter = GoodsIssueAdapterr(mList)
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
                val intent = Intent(this, DetailGoodsIssueActivity::class.java)
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
