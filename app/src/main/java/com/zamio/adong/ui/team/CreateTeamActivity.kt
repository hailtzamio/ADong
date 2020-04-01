package com.zamio.adong.ui.team

import LeaderHoriAdapter
import RestClient
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.R
import com.zamio.adong.model.Worker
import kotlinx.android.synthetic.main.activity_create_team.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateTeamActivity : BaseActivity() {



    override fun getLayout(): Int {
      return  R.layout.activity_create_team
    }

    override fun initView() {
        tvTitle.text = "Tạo Đội Thi Công"
        rightButton.visibility = View.GONE
    }

    override fun initData() {
        tvOk.setOnClickListener {

            if(isEmpty(edtName) || isEmpty(edtAddress) || isEmpty(edtPhone) || isEmpty(edtPhone2)){
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

//            if(edtPlateNumber.text.length < 8 || edtPlateNumber.text.length > 12 ){
////                showToast("Biển số xe sai định dạng")
////                return@setOnClickListener
////            }

            val ids = JsonArray()
            ids.add(1)
            ids.add(2)
            ids.add(3)

            val product = JsonObject()
            product.addProperty("name",edtName.text.toString())
            product.addProperty("address",edtAddress.text.toString())
            product.addProperty("phone",edtPhone.text.toString())
            product.addProperty("phone2",edtPhone2.text.toString())
            product.addProperty("provinceId",1)
            product.addProperty("districtId",1)
            product.addProperty("leaderId",1)
            product.add("memberIds",ids)
            createProduct(product)
        }

        tvChooseTeamLeader.setOnClickListener {
            val intent = Intent(this, ChooseTeamLeaderActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun resumeData() {
        getTeamLeader()
    }

    private fun createProduct(lorry: JsonObject){
        showProgessDialog()
        RestClient().getInstance().getRestService().createTeam(lorry).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if( response!!.body() != null && response!!.body().status == 1){
                    showToast("Tạo thành công")
                    setResult(100)
                    finish()
                } else {
                    val obj = JSONObject(response.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }

    var myList: MutableList<Worker> = mutableListOf<Worker>()
    private fun getTeamLeader(){
        showProgessDialog()
        RestClient().getInstance().getRestService().getWorkers2(0,"").enqueue(object :
            Callback<RestData<ArrayList<Worker>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Worker>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<Worker>>>?, response: Response<RestData<ArrayList<Worker>>>?) {
                dismisProgressDialog()
                if( response!!.body().status == 1){
                    myList = response.body().data!!
                    setupRecyclerView(myList!!)
                }
            }
        })
    }

    private fun setupRecyclerView(data:MutableList<Worker>){
        val mAdapter = LeaderHoriAdapter(data)
        recyclerViewTeamLeader.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL ,false)
        recyclerViewTeamLeader.setHasFixedSize(false)
        recyclerViewTeamLeader.adapter = mAdapter

        mAdapter.onItemClick = { position ->
            myList!!.removeAt(position)
//            mAdapter.notifyDataSetChanged()
            mAdapter.notifyItemRemoved(position)
        }
    }

}
