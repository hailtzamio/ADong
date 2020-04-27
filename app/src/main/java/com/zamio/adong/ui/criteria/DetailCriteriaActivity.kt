package com.zamio.adong.ui.criteria

import RestClient
import SmallCriteriaAdapter
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.Criteria
import com.zamio.adong.model.CriteriaSmall
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_detail_criteria.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCriteriaActivity : BaseActivity() {


    var data: Criteria? = null
    var productId = 1
    override fun getLayout(): Int {
        return R.layout.activity_detail_criteria
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {

            productId = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)


            if (!ConstantsApp.PERMISSION.contains("u")) {
                rightButton.visibility = View.GONE
            }

            if (!ConstantsApp.PERMISSION.contains("d")) {
                tvOk.visibility = View.GONE
            }

            rightButton.setOnClickListener {
                val intent = Intent(this, UpdateCriteriaActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, data!!)
                startActivityForResult(intent, 1000)
                this!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            tvOk.setOnClickListener {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                removeCriteria()
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage("Xóa bộ tiêu chí này?")
                    .setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show()
            }

        }

        getProduct(productId)
    }

    override fun resumeData() {

    }

    private fun getProduct(id: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getCriteria(id).enqueue(object :
            Callback<RestData<Criteria>> {

            override fun onFailure(call: Call<RestData<Criteria>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<Criteria>>?,
                response: Response<RestData<Criteria>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    data = response.body().data ?: return
                    tvName.text = data!!.name
                    tvAuthor.text = data!!.createdByFullName
                    tvDate.text = data!!.createdTime
                    tvEditDate.text = data!!.updatedTime
                    tvEditer.text = data!!.updatedByFullName
                    getSmallCriteria()
                }
            }
        })
    }

    private fun getSmallCriteria() {
        showProgessDialog()
        RestClient().getInstance().getRestService().getSmallCriteria(data!!.id).enqueue(object :
            Callback<RestData<ArrayList<CriteriaSmall>>> {

            override fun onFailure(call: Call<RestData<ArrayList<CriteriaSmall>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<ArrayList<CriteriaSmall>>>?,
                response: Response<RestData<ArrayList<CriteriaSmall>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    setupRecyclerView(response.body().data!!)
                } else {
                    showToast("Không lấy được dữ liệu")
                }
            }
        })
    }

    private fun setupRecyclerView(data: List<CriteriaSmall>) {

        val mAdapter = SmallCriteriaAdapter(data)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            //            val intent = Intent(this, DetailCriteriaActivity::class.java)
//            intent.putExtra(ConstantsApp.KEY_QUESTION_ID, product.id)
//            startActivityForResult(intent,1000)
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun removeCriteria() {
        showProgessDialog()
        RestClient().getInstance().getRestService().removeCriteria(data!!.id).enqueue(object :
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
            getProduct(productId)
        }
    }

}
