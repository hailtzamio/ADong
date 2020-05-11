package com.zamio.adong.ui.project.tab.ui.main.workers

import RestClient
import WorkerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Worker
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.project.tab.ProjectTabActivity
import com.zamio.adong.ui.worker.DetailWorkerActivity
import com.zamio.adong.utils.PaginationScrollListener
import kotlinx.android.synthetic.main.fragment_main_worker.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainWorkerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectWorkersFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var page = 0
    var data = ArrayList<Worker>()
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

        return inflater.inflate(R.layout.fragment_main_workeoutline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        pullToRefresh.setOnRefreshListener(OnRefreshListener {
//            getData(0)
//            pullToRefresh.isRefreshing = false
//        })
        setupRecyclerView()
        getData(page)
    }

    override fun onResume() {
        super.onResume()
    }

    fun getData(pPage:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getProjectWorkers((activity as ProjectTabActivity).getProjectId(),pPage).enqueue(object :
            Callback<RestData<List<Worker>>> {
            override fun onFailure(call: Call<RestData<List<Worker>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<List<Worker>>>?, response: Response<RestData<List<Worker>>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response.body().status == 1){
                    data.addAll(response.body().data!!)
                    mAdapter.notifyDataSetChanged()
                    page += 1
                }
            }
        })
    }

    val mAdapter = WorkerAdapter(data)
    private fun setupRecyclerView(){
        val layoutManager = LinearLayoutManager(context)
        if( recyclerView != null) {
            recyclerView.layoutManager = layoutManager
            recyclerView.setHasFixedSize(false)
            recyclerView.adapter = mAdapter

            mAdapter.onItemClick = { product ->
                val intent = Intent(context, DetailWorkerActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.id)
                intent.putExtra(ConstantsApp.ChooseTeamWorkerActivity, "")
                startActivityForResult(intent,1000)
                activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            recyclerView?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
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
        getData(page)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 101){
            getData(0)
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
            ProjectWorkersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
