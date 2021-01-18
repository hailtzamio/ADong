package com.zamio.adong.ui.notification

import NotificationAdapter
import RestClient
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.adapter.PaginationScrollListener
import com.zamio.adong.model.NotificationOb
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.network.Pagination
import com.zamio.adong.ui.project.tab.ui.main.information.BasicInformation2Activity
import com.zamio.adong.ui.project.tab.ui.main.requirement.DetailProductRequrementActivity
import com.zamio.adong.ui.trip.DetailTripActivity
import com.zamio.adong.ui.ware.stock.goods_issue_request.DetailGoodsIssueRequestActivity
import kotlinx.android.synthetic.main.fragment_main_notification.*
import kotlinx.android.synthetic.main.item_header_layout.*
import kotlinx.android.synthetic.main.item_search_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class NotificationActivity : BaseActivity() {


    var currentPage = 0
    var totalPages = 0
    var page = 0
    var data = ArrayList<NotificationOb>()
    override fun getLayout(): Int {
        return R.layout.activity_notification
    }

    override fun initView() {
        rightButton.visibility = View.GONE
        tvTitle.text = "Thông Báo"
    }

    override fun initData() {
        setupRecyclerView()
    }

    override fun resumeData() {
        resetData()
        getData(0)
    }

    private fun resetData() {
        page = 0
        data.clear()
    }

    private fun getData(pPage: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService()
            .getNotifications(pPage, edtSearch.text.toString())
            .enqueue(object :
                Callback<RestData<ArrayList<NotificationOb>>> {
                override fun onFailure(
                    call: Call<RestData<ArrayList<NotificationOb>>>?,
                    t: Throwable?
                ) {
                    dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<ArrayList<NotificationOb>>>?,
                    response: Response<RestData<ArrayList<NotificationOb>>>?
                ) {
                    dismisProgressDialog()
                    if (response!!.body() != null && response.body().status == 1) {
                        data.addAll(response.body().data!!)
                        totalPages = response.body().pagination!!.totalPages!!
                        mAdapter.notifyDataSetChanged()
                        page += 1

                        if(data.size == 0) {
                            viewNoData.visibility = View.VISIBLE
                        } else {
                            viewNoData.visibility = View.GONE
                        }

                        val pagination = response.body().pagination!!
                        if (pagination.totalRecords != null) {
                            showHintText(pagination)
                        }
                    }
                }
            })
    }

    private fun showHintText(pagination: Pagination) {

        if (pagination.totalRecords != null) {

            var count = pagination.totalRecords!!.toString()

            if (pagination.totalRecords!! > 10000) {
                count = "10000+"
            }

            edtSearch.hint = "Tìm kiếm trong $count công nhân"
        }
    }

    var mAdapter = NotificationAdapter(data)
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    private fun setupRecyclerView() {

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { it ->

            val objectId = it.objectId
            readNotification(it.id!!)
            when (it.objectType) {

                "Project" -> {
                    val intent = Intent(this, BasicInformation2Activity::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_ID, objectId)
                    startActivity(intent)
                }

                "ProductRequirement" -> {
                    val intent = Intent(this, DetailProductRequrementActivity::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_ID_PR, objectId)
                    startActivity(intent)
                }

                "GoodsIssueReq" -> {
                    val intent = Intent(this, DetailGoodsIssueRequestActivity::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_ID, objectId)
                    startActivity(intent)
                }

                "NEW_TRIP" -> {
                    val intent = Intent(this, DetailTripActivity::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_ID, objectId)
                    startActivity(intent)
                }
            }
        }

        recyclerView?.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
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

    fun getMoreItems() {
        isLoading = false
        if (page < totalPages) {
            getData(page)
        }
    }

    private fun readNotification(id:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getNotification(id).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response!!.body().status == 1){

                }
            }
        })
    }


}
