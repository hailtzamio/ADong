package com.zamio.adong.ui.product.home

import ProductAdapter
import RestClient
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Product
import com.zamio.adong.ui.product.CreateProductActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgessDialog()
        getProducts()

        rightButton.setOnClickListener {
            val intent = Intent(context, CreateProductActivity::class.java)
            intent.putExtra("EMAIL", "")
            startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

    }

    private fun getProducts(){
        RestClient().getRestService().getProducts().enqueue(object :
            Callback<RestData<List<Product>>> {
            override fun onFailure(call: Call<RestData<List<Product>>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<RestData<List<Product>>>?, response: Response<RestData<List<Product>>>?) {
                    dismisProgressDialog()
                    Log.e("hailpt", " Data = " + response!!.body().data )
                    if( response!!.body().status == 1){
                        setupRecyclerView(response.body().data!!)
                    }
            }
        })
    }

    private fun setupRecyclerView(data:List<Product>){
        val mAdapter = ProductAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter
    }
}
