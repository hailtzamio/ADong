package com.zamio.adong.ui.criteria

import CriteriaCreateAdapter
import RestClient
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.CriteriaSmall
import com.zamio.adong.model.CriteriaValues
import kotlinx.android.synthetic.main.activity_create_criteria.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateCriteriaActivity : BaseActivity() {


    var product = JSONObject()
    override fun getLayout(): Int {
        return R.layout.activity_create_criteria
    }

    override fun initView() {
        tvTitle.text = "Tạo Bộ Tiêu Chí"
        rightButton.visibility = View.GONE
    }

    override fun initData() {

        tvOk.setOnClickListener {

            if (isEmpty(edtName)) {
                showToast("Nhập tên bộ tiêu chí")
                return@setOnClickListener
            }

            var isOk = false
            for (i in data.size - 1 downTo 0) {
                if (data[i].name != "" && data[i].factor != 0) {
                    isOk = true
                }
            }

            if (!isOk) {
                showToast("Nhập tiêu chí")
                return@setOnClickListener
            }

            for (i in data.size - 1 downTo 0) {
                if (data[i].name == "" || data[i].factor == 0) {
                    data.removeAt(i)
                }
            }

            createProduct(CriteriaValues(data, edtName.text.toString()))
        }
        setupRecyclerView()
    }

    var data = ArrayList<CriteriaSmall>();
    var mAdapter: CriteriaCreateAdapter? = null
    private fun setupRecyclerView() {
        data.add(CriteriaSmall(0, 0, "", "new"))
        data.add(CriteriaSmall(0, 0, "","new"))
        mAdapter = CriteriaCreateAdapter(data!!)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter!!.onItemClick = { position ->
            data.add(CriteriaSmall(0, 0, "","new"))
            mAdapter!!.notifyItemInserted(position + 1)
            mAdapter!!.notifyItemChanged(position)
            recyclerView.layoutManager!!.scrollToPosition(position + 1)
        }
    }

    override fun resumeData() {

    }

    private fun createProduct(product: CriteriaValues) {
        showProgessDialog()
        RestClient().getInstance().getRestService().createCriteria(product).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response?.body() != null && response.body().status == 1) {
                    showToast("Tạo thành công")
                    setResult(100)
                    finish()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }
}
