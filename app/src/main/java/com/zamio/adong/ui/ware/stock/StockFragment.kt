package com.zamio.adong.ui.ware.stock

import RestClient
import WareHouseAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.WareHouse
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.ware.stock.stock.StockListTitleActivity
import kotlinx.android.synthetic.main.fragment_main_workeoutline.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class StockFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null
    var currentPage = 0
    var totalPages = 0
    var type = "STOCK"
    var mList = ArrayList<WareHouse>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_workeoutline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getData()

    }

    override fun onResume() {
        super.onResume()

        mList.clear()
        getData()

    }

    private fun getData() {
        showProgessDialog()
        RestClient().getInstance().getRestService()
            .getStocks("",type)
            .enqueue(object :
                Callback<RestData<ArrayList<WareHouse>>> {
                override fun onFailure(call: Call<RestData<ArrayList<WareHouse>>>?, t: Throwable?) {
                    dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<ArrayList<WareHouse>>>?,
                    response: Response<RestData<ArrayList<WareHouse>>>?
                ) {
                    dismisProgressDialog()
                    if (response!!.body() != null && response.body().status == 1) {
                        mList = response.body().data!!
                        setupRecyclerView2()

                    }
                }
            })
    }


    private fun setupRecyclerView2() {
        if (recyclerView != null) {
            val mAdapter = WareHouseAdapter(mList)
            recyclerView.layoutManager = LinearLayoutManager(context!!)
            recyclerView.setHasFixedSize(false)
            recyclerView.adapter = mAdapter

            mAdapter.onItemClick = { product ->
                val intent = Intent(context, StockListTitleActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_STATUS, type)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.id)
                startActivityForResult(intent, 1000)
            }
        }
    }



    companion object {
        fun newInstance(param1: String, param2: String) =
            StockFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
