package com.zamio.adong.ui.driver

import RestClient
import TripAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.adapter.PaginationScrollListener
import com.zamio.adong.model.Trip
import com.zamio.adong.model.TripRq
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.trip.DetailTripActivity
import com.zamio.adong.ui.trip.TripActivity
import kotlinx.android.synthetic.main.activity_driver_action.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverActionActivity : BaseActivity() {

    var currentPage = 0
    var totalPages = 0
    var data: List<Trip>? = null
    override fun getLayout(): Int {
       return R.layout.activity_driver_action
    }

    override fun initView() {
        rightButton.visibility = View.GONE
        tvTitle.text = "Chuyến Đi"
    }

    override fun initData() {

    }

    override fun resumeData() {
        getData(0)
    }

    private fun getData(page: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getTrips(page, "").enqueue(object :
            Callback<RestData<ArrayList<Trip>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Trip>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<ArrayList<Trip>>>?,
                response: Response<RestData<ArrayList<Trip>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    data = response.body().data!!

                    if (data != null && data!!.isNotEmpty()) {
                        viewNoData.visibility = View.GONE
                        setupRecyclerView()
                    } else {
                        viewNoData.visibility = View.VISIBLE
                    }

                    totalPages = response.body().pagination!!.totalPages!!

                }
            }
        })
    }

    private fun getDataNewTrip(page: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getTripsNew(page, "").enqueue(object :
            Callback<RestData<ArrayList<Trip>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Trip>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<ArrayList<Trip>>>?,
                response: Response<RestData<ArrayList<Trip>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    data = response.body().data!!

                    if (data != null && data!!.isNotEmpty()) {
                        viewNoData.visibility = View.GONE
                        setupRecyclerView()
                    } else {
                        viewNoData.visibility = View.VISIBLE
                    }

                    totalPages = response.body().pagination!!.totalPages!!

//                        val pagination = response.body().pagination!!
//                        if (pagination.totalRecords != null) {
//                            showHintText(pagination)
//                        }
                }
            }
        })
    }

    private fun setupRecyclerView() {

        val mAdapter = TripAdapter(data!!)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { it ->

            val transportReqIds = ArrayList<Int>()

            ConstantsApp.transportsChoose.forEach {
                if (it.isSelected == true) {
                    transportReqIds.add(it.id ?: 0)
                }
            }

            val intent = Intent(this, DetailTripActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, it.id)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        var isLastPage: Boolean = false
        var isLoading: Boolean = false

        recyclerView?.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                if ((currentPage + 1) < totalPages) {
                    getData(currentPage++)
                }
                currentPage += 1
            }
        })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 100) {
//            getProducts(0)
        }
    }


}
