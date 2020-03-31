package com.zamio.adong.ui.lorry

import LorryAdapter
import RestClient
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Lorry
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.fragment_main_lorry_list.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [MainLorryFragment.OnListFragmentInteractionListener] interface.
 */
class MainLorryFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_lorry_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!ConstantsApp.PERMISSION!!.contains("c")){
            rightButton.visibility = View.GONE
        }

        rightButton.setOnClickListener {
            val intent = Intent(context, CreateLorryActivity::class.java)
            startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onResume() {
        super.onResume()
        getProducts()
    }


    private fun getProducts(){
        showProgessDialog()
        RestClient().getInstance().getRestService().getLorries().enqueue(object :
            Callback<RestData<List<Lorry>>> {
            override fun onFailure(call: Call<RestData<List<Lorry>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<List<Lorry>>>?, response: Response<RestData<List<Lorry>>>?) {
                dismisProgressDialog()
                if( response!!.body().status == 1){
                    setupRecyclerView(response.body().data!!)
                }
            }
        })
    }

    private fun setupRecyclerView(data:List<Lorry>){
        val mAdapter = LorryAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            val intent = Intent(context, DetailLorryActivity::class.java)
//            intent.putExtra(ConstantsApp.KEY_PERMISSION, actionString)
            intent.putExtra(ConstantsApp.KEY_QUESTION_ID, product.id)
            startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 100){
            getProducts()
        }
    }

}
