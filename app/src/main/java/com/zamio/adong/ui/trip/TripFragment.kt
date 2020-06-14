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
import com.zamio.adong.R
import com.zamio.adong.adapter.PaginationScrollListener
import com.zamio.adong.model.Trip
import kotlinx.android.synthetic.main.activity_overview_project.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TripFragment : BaseFragment() {

    var currentPage = 0
    var totalPages = 0
    var data:List<Trip>? = null
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
        getData(0)
    }

    private fun getData(page:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getTrips(page, "").enqueue(object :
            Callback<RestData<ArrayList<Trip>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Trip>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<Trip>>>?, response: Response<RestData<ArrayList<Trip>>>?) {
                    dismisProgressDialog()
                    if(response!!.body() != null && response!!.body().status == 1){
                        data = response.body().data!!
                        setupRecyclerView()
                        if(data!!.isEmpty()) {
                            showToast("Danh sách trống")
                        }
                    }
            }
        })
    }

    private fun setupRecyclerView(){

        val mAdapter = TripAdapter(data!!)
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { it ->
//            val intent = Intent(context, DetailTransportActivity::class.java)
//            intent.putExtra(ConstantsApp.KEY_VALUES_ID, it.id)
//            startActivityForResult(intent,1000)
//            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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
