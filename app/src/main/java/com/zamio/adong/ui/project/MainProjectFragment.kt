package com.zamio.adong.ui.project

import ProjectAdapter
import RestClient
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Project
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.project.tab.ProjectTabActivity
import com.zamio.adong.utils.PaginationScrollListener
import kotlinx.android.synthetic.main.fragment_main_team_list.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [MainProjectFragment.OnListFragmentInteractionListener] interface.
 */
class MainProjectFragment : BaseFragment() {

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var page = 0
    var data = ArrayList<Project>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_team_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!ConstantsApp.PERMISSION!!.contains("c")){
            rightButton.visibility = View.GONE
        }

        rightButton.setOnClickListener {
            val intent = Intent(context, CreateProjectActivity::class.java)
            startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getProducts()
                return@OnEditorActionListener true
            }
            false
        })

        imvSearch.setOnClickListener {
            getProducts()
        }

        setupRecyclerView(data)
    }

    override fun onResume() {
        super.onResume()
        getProducts()
    }


    private fun getProducts(){
        showProgessDialog()
        RestClient().getInstance().getRestService().getProjects(page,edtSearch.text.toString()).enqueue(object :
            Callback<RestData<List<Project>>> {
            override fun onFailure(call: Call<RestData<List<Project>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<List<Project>>>?, response: Response<RestData<List<Project>>>?) {
                dismisProgressDialog()
                if( response!!.body() != null && response!!.body().status == 1){
                    data.addAll(response.body().data!!)
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    var layoutManager = LinearLayoutManager(context)
    val mAdapter = ProjectAdapter(data)
    private fun setupRecyclerView(data:List<Project>){
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { data ->
            val intent = Intent(context, ProjectTabActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, data.id)
            startActivity(intent)
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


    fun getMoreItems() {
        isLoading = false
        page += 1
        getProducts()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 100){
//            getProducts()
        }
    }

}
