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
import com.zamio.adong.ui.lorry.map.LorryLocationActivity
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

        imvMap.setOnClickListener {
            val intent = Intent(context, LorryLocationActivity::class.java)
//            intent.putExtra(ConstantsApp.KEY_QUESTION_ID, lorry!!)
            startActivity(intent)
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
                if(response!!.body() != null && response!!.body().status == 1){
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
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.id)
            startActivityForResult(intent,1000)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 100){
//            getProducts()
        }
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): MainLorryFragment {
            return MainLorryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

}
