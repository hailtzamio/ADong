package com.zamio.adong.ui.project.tab.ui.main.requirement

import ProductRequirementAdapter
import RestClient
import SwipeToDeleteCallback
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.ProductRequirement
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.fragment_main_product.*
import kotlinx.android.synthetic.main.fragment_main_worker.recyclerView
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
class ProductRequirementFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var idddd = 0
    var data:ArrayList<ProductRequirement>? = null
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
        return inflater.inflate(R.layout.fragment_main_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData(0)
    }


    override fun onResume() {
        super.onResume()

    }

    fun setProjectId(id:Int) {
        idddd = id
    }

    fun getData(page:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getProductRequirement(idddd,page).enqueue(object :
            Callback<RestData<ArrayList<ProductRequirement>>> {
            override fun onFailure(call: Call<RestData<ArrayList<ProductRequirement>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<ProductRequirement>>>?, response: Response<RestData<ArrayList<ProductRequirement>>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response.body().status == 1){
                    data = response.body().data!!

                    if (data!!.isNotEmpty()) {
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
        if ( recyclerView != null ) {
            val mAdapter = ProductRequirementAdapter(data!!)
            val linearLayoutManager = LinearLayoutManager(context)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.setHasFixedSize(false)
            recyclerView.adapter = mAdapter

            mAdapter.onItemClick = { product ->
                val intent = Intent(context, DetailProductRequrementActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID_PR, product.id)
                startActivityForResult(intent,1000)
                activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            val swipeHandler = object : SwipeToDeleteCallback(activity!!) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = recyclerView.adapter as ProductRequirementAdapter
                    adapter.removeAt(viewHolder.adapterPosition)
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
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
            ProductRequirementFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
