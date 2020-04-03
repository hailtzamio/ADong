package com.zamio.adong.ui.team

import RestClient
import TeamAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Team
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.fragment_main_team_list.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [MainTeamFragment.OnListFragmentInteractionListener] interface.
 */
class MainTeamFragment : BaseFragment() {

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
            val intent = Intent(context, CreateTeamActivity::class.java)
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
        RestClient().getInstance().getRestService().getTeams(0).enqueue(object :
            Callback<RestData<List<Team>>> {
            override fun onFailure(call: Call<RestData<List<Team>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<List<Team>>>?, response: Response<RestData<List<Team>>>?) {
                dismisProgressDialog()
                if( response!!.body() != null && response!!.body().status == 1){
                    setupRecyclerView(response.body().data!!)
                }
            }
        })
    }

    private fun setupRecyclerView(data:List<Team>){
        val mAdapter = TeamAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            val intent = Intent(context, DetailTeamActivity::class.java)
//            intent.putExtra(ConstantsApp.KEY_PERMISSION, actionString)
            intent.putExtra(ConstantsApp.KEY_QUESTION_ID, product.id)
            startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 100){
//            getProducts()
        }
    }

}
