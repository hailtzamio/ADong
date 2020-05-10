package com.zamio.adong.ui.workoutline

import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.WorkOutline
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_detail_workeoutline.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailWorkOutlineActivity : BaseActivity() {


    var data: WorkOutline? = null
    var id = 1
    override fun getLayout(): Int {
        return R.layout.activity_detail_workeoutline
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {

            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)


            if (!ConstantsApp.PERMISSION.contains("u")) {
                rightButton.visibility = View.GONE
            }

            if (!ConstantsApp.PERMISSION.contains("d")) {
                tvOk.visibility = View.GONE
            }

            rightButton.setOnClickListener {
                val intent = Intent(this, UpdateWorkOutlineActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, data!!)
                startActivityForResult(intent, 1000)
                this!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            tvOk.setOnClickListener {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                remove()
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage("Chắc chắn xóa?")
                    .setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show()
            }

            if (intent.hasExtra(ConstantsApp.KEY_VALUES_HIDE)) {
                tvOk.visibility = View.GONE
                rightButton.visibility = View.GONE
            }

        }

        getData(id)
    }

    override fun resumeData() {

    }

    private fun getData(id: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getWorkOutline(id).enqueue(object :
            Callback<RestData<WorkOutline>> {

            override fun onFailure(call: Call<RestData<WorkOutline>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<WorkOutline>>?,
                response: Response<RestData<WorkOutline>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    data = response.body().data ?: return
                    tvName.text = data!!.name
                    tvAuthor.text = data!!.createdByFullName
                    tvDate.text = data!!.createdTime
                    tvEditDate.text = data!!.updatedTime
                    tvEditer.text = data!!.updatedByFullName
                    tvSequence.text = data!!.sequence.toString()
                }
            }
        })
    }

    private fun remove() {
        showProgessDialog()
        RestClient().getInstance().getRestService().removeWorkOutline(data!!.id).enqueue(object :
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
                    showToast("Xóa thành công")
                    setResult(100)
                    finish()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 100) {
            getData(id)
        }
    }

}
