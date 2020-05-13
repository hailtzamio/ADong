package com.zamio.adong.ui.criteria

import CriteriaAdapter
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
import com.zamio.adong.model.Criteria
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.fragment_main_worker.*
import kotlinx.android.synthetic.main.item_header_layout.*
import kotlinx.android.synthetic.main.item_search_layout.*
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
class MainCriteriaFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    var currentPage = 0
    var totalPages = 0
    var data:List<Criteria>? = null
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
        return inflater.inflate(R.layout.fragment_main_criteria, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getProducts(0)
        rightButton.setOnClickListener {
            val intent = Intent(context, CreateCriteriaActivity::class.java)
            intent.putExtra("EMAIL", "")
            startActivityForResult(intent,1000)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        imvBack.setOnClickListener {
            activity!!.onBackPressed()
        }

        if(!ConstantsApp.PERMISSION!!.contains("c")){
            rightButton.visibility = View.GONE
        }

        edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getProducts(0)
                return@OnEditorActionListener true
            }
            false
        })

        imvSearch.setOnClickListener {
            getProducts(0)
        }
    }

    override fun onResume() {
        super.onResume()
        getProducts(0)
    }

    private fun getProducts(page:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getCriterias(page,edtSearch.text.toString()).enqueue(object :
            Callback<RestData<List<Criteria>>> {
            override fun onFailure(call: Call<RestData<List<Criteria>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<List<Criteria>>>?, response: Response<RestData<List<Criteria>>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response!!.body().status == 1){
                    data = response.body().data!!
                    setupRecyclerView()
                    totalPages = response.body().pagination!!.totalPages!!
                }
            }
        })
    }

    private fun setupRecyclerView(){

        val mAdapter = CriteriaAdapter(data!!)
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            val intent = Intent(context, DetailCriteriaActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.id)
            startActivityForResult(intent,1000)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 100){
//            getProducts(0)
        }
    }
}
