package com.zamio.adong.ui.team

import ChooseWorkerAdapter
import RestClient
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Worker2
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.worker.DetailWorkerActivity
import kotlinx.android.synthetic.main.activity_choose_team_worker.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChooseTeamWorkerActivity : BaseActivity() {

    var currentPage = 0
    var totalPages = 0
    var workers = ArrayList<Worker2>()
    var workersChoose = ArrayList<Worker2>()
    override fun getLayout(): Int {
        return R.layout.activity_choose_team_worker
    }

    override fun initView() {

    }

    override fun initData() {
        getProducts(0)
        imvBack.setOnClickListener {
            onBackPressed()
        }

        if (!ConstantsApp.PERMISSION!!.contains("c")) {
            rightButton.visibility = View.GONE
        }

        edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getProducts(0)
                return@OnEditorActionListener true
            }
            false
        })

        tvTitle.text = "Danh Sách Công Nhân"
        rightButton.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putParcelableArrayListExtra("workersChoose", workersChoose )
            ConstantsApp.workers = workersChoose
            setResult(101, returnIntent)
            finish()
        }
    }

    override fun resumeData() {

    }

    override fun onResume() {
        super.onResume()

    }

    private fun getProducts(page: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getWorkers2(page, edtSearch.text.toString())
            .enqueue(object :
                Callback<RestData<ArrayList<Worker2>>> {
                override fun onFailure(call: Call<RestData<ArrayList<Worker2>>>?, t: Throwable?) {
                    dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<ArrayList<Worker2>>>?,
                    response: Response<RestData<ArrayList<Worker2>>>?
                ) {
                    dismisProgressDialog()
                    if (response!!.body().status == 1) {
                        workers = response.body().data!!
                        for (i in ( workers.size -1) downTo 0){
                            if(workers[i].isTeamLeader){
                                workers.removeAt(i)
                            }
                        }
                        setupRecyclerView()
                        totalPages = response.body().pagination!!.totalPages!!
                    }
                }
            })
    }

    private fun setupRecyclerView() {
        val mAdapter = ChooseWorkerAdapter(workers!!)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            val intent = Intent(this, DetailWorkerActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_QUESTION_ID, product.id)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        mAdapter.onItemSelected = { position, isChecked ->
            workers[position].isSelected = isChecked

            if (isChecked) {
                for (j in ( workersChoose.size -1) downTo 0){
                        if(workersChoose[j].id == workers[position].id){
//                            return
                        }
                }


                workersChoose.add(workers[position])
            } else {
                for (i in workersChoose.indices) {
                    for (j in ( workersChoose.size -1) downTo 0){
                        if (workers[position].id == workersChoose[j].id) {
                            workersChoose.removeAt(j)
                        }
                    }
                }
            }

            for (i in workersChoose.indices) {
                Log.e("hailpt", "workersChoose  " + workersChoose[i].id)
            }
        }
    }
}
