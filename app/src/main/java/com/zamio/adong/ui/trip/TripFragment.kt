package com.zamio.adong.ui.trip

import RestClient
import TripAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.ui.network.UserRoles
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.adapter.PaginationScrollListener
import com.zamio.adong.model.Transport
import com.zamio.adong.model.Trip
import com.zamio.adong.model.TripRq
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_overview_project.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TripFragment : BaseFragment() {

    var currentPage = 0
    var totalPages = 0
    var data: List<Trip>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.activity_overview_project, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()

        if(activity is TripTabActivity) {
            (activity as TripTabActivity).setTrips(ArrayList<Transport>())
            getData(0)
        } else {
            getDataNewTrip(0)
        }
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
        val linearLayoutManager = LinearLayoutManager(context)
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

            if (activity is TripActivity) {
                val tripRq = TripRq(
                    it.plannedDatetime ?: "",
                    it.driverId ?: 1,
                    it.lorryId ?: 1,
                    "",
                    transportReqIds
                )
                createTrip(tripRq)
            } else {
                if(ConstantsApp.USER_ROLES.contains(UserRoles.Driver.type)) {
                    val intent = Intent(context, DetailTripActivity::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_ID, it.id)
                    startActivityForResult(intent, 1000)
                    activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    showToast("Bạn không phải lái xe")
                }

            }
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

    private fun createTrip(tripRq: TripRq) {
        showProgessDialog()
        RestClient().getInstance().getRestService().createTrip(tripRq).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    showToast("Thành công")
                    activity!!.setResult(100)
                    activity!!.finish()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    showToast(obj["message"].toString())
                }
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
