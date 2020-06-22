package com.zamio.adong.ui.trip

import RestClient
import TransportAdapter
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
import com.zamio.adong.model.Transport
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_checkin_out_album_image.*
import kotlinx.android.synthetic.main.activity_overview_project.*
import kotlinx.android.synthetic.main.activity_overview_project.viewNoData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransportRequestFragment : BaseFragment() {

    var currentPage = 0
    var totalPages = 0
    var data = ArrayList<Transport>()
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
        ConstantsApp.transportsChoose.clear()
    }

    private fun getData(page:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getTransports(page, "").enqueue(object :
            Callback<RestData<ArrayList<Transport>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Transport>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<Transport>>>?, response: Response<RestData<ArrayList<Transport>>>?) {
                    dismisProgressDialog()
                    if(response!!.body() != null && response!!.body().status == 1){
                        data = response.body().data ?: return

                        for(i in data.size - 1 downTo 0) {
                            if(data[i].status != 1) {
                                data.removeAt(i)
                            }
                        }

                        if (data.isNotEmpty()) {
                            viewNoData.visibility = View.GONE
                            setupRecyclerView()
                        } else {
                            viewNoData.visibility = View.VISIBLE
                        }
                    }
            }
        })
    }

    private fun setupRecyclerView(){

        val mAdapter = TransportAdapter(data!!)
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


        mAdapter.onItemSelected = { position, isChecked ->
            data[position].isSelected = isChecked
            ( activity as TripTabActivity).setTrips(data)
//            for (i in data.indices) {
//                if(data[i].isSelected == true) {
//                    Log.e("hailpt", "workersChoose  " + data[i].code)
//                }
//            }
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
