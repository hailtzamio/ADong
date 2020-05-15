package com.zamio.adong.ui.project.tab.ui.main.checkinout

import RestClient
import WorkOutlineAdapter
import WorkerAdapter
import WorkerCheckinOutAdapter
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.adapter.PaginationScrollListener
import com.zamio.adong.model.CheckinOut
import com.zamio.adong.model.Worker
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_check_in_out.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckInOutActivity : BaseActivity() {

    var page = 0
    var data = ArrayList<Worker>()
    var id = 0
    override fun getLayout(): Int {
        return R.layout.activity_check_in_out
    }

    override fun initView() {
        tvTitle.text = "Chấm Công"
        rightButton.setOnClickListener {
//            val intent = Intent(this, CheckinOutAlbumActivity::class.java)
//            intent.putExtra(ConstantsApp.KEY_VALUES_ID, id)
//            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun initData() {

        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)
            setupRecyclerView()
            getData(page)
        }
    }

    override fun resumeData() {

    }

    private fun getData(pPage: Int) {
        data.clear()
        showProgessDialog()
        RestClient().getInstance().getRestService().getProjectWorkers(id, pPage).enqueue(object :
            Callback<RestData<List<Worker>>> {
            override fun onFailure(call: Call<RestData<List<Worker>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<List<Worker>>>?,
                response: Response<RestData<List<Worker>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    data.addAll(response.body().data!!)
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    val mAdapter = WorkerCheckinOutAdapter(data)
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        if (recyclerView != null) {
            recyclerView.layoutManager = layoutManager
            recyclerView.setHasFixedSize(false)
            recyclerView.adapter = mAdapter

            mAdapter.onItemClick = { product ->
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                val ids = ArrayList<Int>()
                                ids.add(product.id)

                                val check = CheckinOut(id, ids)
                                if (product.workingStatus == "idle") {
                                    checkin(check)
                                } else {
                                    checkout(check)
                                }
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage("Điểm danh?")
                    .setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show()
            }
        }
    }

    private fun checkout(checkinOut: CheckinOut) {
        showProgessDialog()
        RestClient().getInstance().getRestService().checkout(checkinOut).enqueue(object :
            Callback<RestData<JsonElement>> {
            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    showToast("Chấm giờ ra thành công")
                    getData(page)
                }
            }
        })
    }

    private fun checkin(checkinOut: CheckinOut) {
        showProgessDialog()
        RestClient().getInstance().getRestService().checkin(checkinOut).enqueue(object :
            Callback<RestData<JsonElement>> {
            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    showToast("Chấm giờ vào thành công")
                    getData(page)
                }
            }
        })
    }


}
