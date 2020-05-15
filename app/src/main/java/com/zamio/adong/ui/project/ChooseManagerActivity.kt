package com.zamio.adong.ui.project

import ChooseTeamLeaderAdapter
import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Worker
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_choose_team_leader.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChooseManagerActivity : BaseActivity() {

    var key = 0
    override fun getLayout(): Int {
        return R.layout.activity_choose_to_make_project
    }

    override fun initView() {

        rightButton.visibility = View.GONE

    }

    override fun initData() {


        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            key = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 0)

            when (key) {
                1 -> getManagers()
                2 -> getDeputyManagers()
                3 -> getLeaders()
                4 -> getSecretaries()
            }

            when (key) {
                1 -> tvTitle.text = "Trưởng Bộ Phận"
                2 -> tvTitle.text = "Phó Bộ Phận"
                3 -> tvTitle.text = "Giám Sát"
                4 -> tvTitle.text = "Thư Ký"
            }

        }
    }

    override fun resumeData() {

    }

    private fun getManagers() {
        showProgessDialog()
        RestClient().getInstance().getRestService().getManagers(0, "").enqueue(object :
            Callback<RestData<List<Worker>>> {
            override fun onFailure(call: Call<RestData<List<Worker>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<List<Worker>>>?,
                response: Response<RestData<List<Worker>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body().status == 1) {
                    setupRecyclerView(response.body().data!!)
                }
            }
        })
    }

    private fun getDeputyManagers() {
        showProgessDialog()
        RestClient().getInstance().getRestService().getDeputyManagers(0, "").enqueue(object :
            Callback<RestData<List<Worker>>> {
            override fun onFailure(call: Call<RestData<List<Worker>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<List<Worker>>>?,
                response: Response<RestData<List<Worker>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body().status == 1) {
                    setupRecyclerView(response.body().data!!)
                }
            }
        })
    }

    private fun getLeaders() {
        showProgessDialog()
        RestClient().getInstance().getRestService().getTeamLeaders(0, "").enqueue(object :
            Callback<RestData<List<Worker>>> {
            override fun onFailure(call: Call<RestData<List<Worker>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<List<Worker>>>?,
                response: Response<RestData<List<Worker>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body().status == 1) {
                    setupRecyclerView(response.body().data!!)
                }
            }
        })
    }

    private fun getSecretaries() {
        showProgessDialog()
        RestClient().getInstance().getRestService().getSecretaries(0, "").enqueue(object :
            Callback<RestData<List<Worker>>> {
            override fun onFailure(call: Call<RestData<List<Worker>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<List<Worker>>>?,
                response: Response<RestData<List<Worker>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body().status == 1) {
                    setupRecyclerView(response.body().data!!)
                }
            }
        })
    }

    private fun setupRecyclerView(data: List<Worker>) {
        val mAdapter = ChooseTeamLeaderAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { worker ->
            showPopupChooseLeader(worker)
        }
    }

    private fun showPopupChooseLeader(worker: Worker) {
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val returnIntent = Intent()
                        returnIntent.putExtra("id", worker.id)
                        returnIntent.putExtra("name", worker.fullName)
                        setResult(key, returnIntent)
                        finish()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Chọn " + worker.fullName + " ?")
            .setPositiveButton("Đồng ý", dialogClickListener)
            .setNegativeButton("Không", dialogClickListener).show()
    }
}
