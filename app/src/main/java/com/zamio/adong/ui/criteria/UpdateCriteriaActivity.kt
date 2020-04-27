package com.zamio.adong.ui.criteria

import CriteriaCreateAdapter
import RestClient
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.Criteria
import com.zamio.adong.model.CriteriaSmall
import com.zamio.adong.model.Update
import com.zamio.adong.model.UpdateCriteria
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_create_criteria.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateCriteriaActivity : BaseActivity() {


    var isTeamLeader = false
    var id = 0
    var data = ArrayList<CriteriaSmall>();
    var addNewList = ArrayList<CriteriaSmall>();
    var removeList = ArrayList<Int>();
    var updateList = ArrayList<Update>();
    var mAdapter: CriteriaCreateAdapter? = null
    override fun getLayout(): Int {
        return R.layout.activity_update_criterial
    }

    override fun initView() {
        rightButton.visibility = View.GONE
        tvTitle.text = "Cập Nhật"
    }

    override fun initData() {

        val criteriaOb = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as Criteria
        id = criteriaOb.id
        edtName.setText(criteriaOb.name)

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
                    if (data[i].status != "new") {
                        removeList.add(data[i].id)
                        data.removeAt(i)
                    }
                }
            }

            // add new list
            for (i in data.size - 1 downTo 0) {
                if(data[i].status == "new" && data[i].name != "") {
                    addNewList.add(data[i])
                    data.removeAt(i)
                }
            }

            for (i in data.size - 1 downTo 0) {
                if (data[i].name == "" || data[i].factor == 0) {
                    data.removeAt(i)
                }
            }

            // Add Update
            data.forEach {
                updateList.add(Update(id,it.factor,it.id,it.name))
            }

            val updateOb = UpdateCriteria(addNewList,edtName.text.toString(),removeList,updateList)

            update(updateOb)
        }

        getSmallCriteria()
    }


    private fun setupRecyclerView(data: ArrayList<CriteriaSmall>) {
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

    private fun getSmallCriteria() {
        showProgessDialog()
        RestClient().getInstance().getRestService().getSmallCriteria(id).enqueue(object :
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
                    data = response.body().data!!
                    setupRecyclerView(response.body().data!!)
                } else {
                    showToast("Không lấy được dữ liệu")
                }
            }
        })
    }

    override fun resumeData() {

    }

    private fun update(product: UpdateCriteria) {
        showProgessDialog()
        RestClient().getInstance().getRestService().updateCriteria(id, product).enqueue(object :
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
                    showToast("Sửa thành công")
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
