package com.zamio.adong.ui

import NotificationAdapter
import RestClient
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.adapter.PaginationScrollListener
import com.zamio.adong.model.NotificationOb
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.network.Pagination
import com.zamio.adong.ui.project.tab.ui.main.information.BasicInformation2Activity
import com.zamio.adong.ui.project.tab.ui.main.information.BasicInformationActivity
import com.zamio.adong.ui.trip.DetailTripActivity
import kotlinx.android.synthetic.main.fragment_main_notification.*
import kotlinx.android.synthetic.main.fragment_main_worker.*
import kotlinx.android.synthetic.main.fragment_main_worker.recyclerView
import kotlinx.android.synthetic.main.item_header_layout.*
import kotlinx.android.synthetic.main.item_search_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainWorkerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    var currentPage = 0
    var totalPages = 0
    var page = 0
    var data = ArrayList<NotificationOb>()
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
        return inflater.inflate(R.layout.fragment_main_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTitle.text = "Thông Báo"
        rightButton.visibility = View.GONE

        edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                resetData()
                getData(0)
                return@OnEditorActionListener true
            }
            false
        })

        imvSearch.setOnClickListener {
            resetData()
            getData(0)
        }

        setupRecyclerView()

    }

    override fun onResume() {
        super.onResume()
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

        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { it ->

            val objectId = it.objectId
            when (it.type) {

                "REG_APPROVED" -> {
                    val intent = Intent(activity, BasicInformation2Activity::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_REG_APPROVED, objectId)
                    startActivity(intent)
                }

                "NEW_PROJECT" -> {
                    val intent = Intent(activity, BasicInformation2Activity::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_ID, objectId)
                    intent.putExtra(ConstantsApp.KEY_VALUES_NEW_PROJECT, objectId)
                    startActivity(intent)
                }

                "NEW_TRIP" -> {
                    val intent = Intent(activity, DetailTripActivity::class.java)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 100) {
//            getProducts(0)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainWorkerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
