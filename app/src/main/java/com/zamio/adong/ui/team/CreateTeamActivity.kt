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
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.model.Worker2
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_create_team.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateTeamActivity : BaseActivity() {


    var mAdapter:LeaderHoriAdapter? = null
    var leaderId = 0
    var workerIds = JsonArray()
    override fun getLayout(): Int {
      return  R.layout.activity_create_team
    }

    override fun initView() {
        tvTitle.text = "Tạo Đội Thi Công"
        rightButton.visibility = View.GONE
    }

    override fun initData() {
        getTeamLeader()
        tvOk.setOnClickListener {

            if(isEmpty(edtName) || isEmpty(edtAddress) || isEmpty(edtPhone) || isEmpty(edtPhone2)){
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            if(leaderId == 0) {
                showToast("Chọn Đội trưởng")
                return@setOnClickListener
            }

            if(workerIds.count() == 0) {
                showToast("Chọn Công nhân")
                return@setOnClickListener
            }

            val product = JsonObject()
            product.addProperty("name",edtName.text.toString())
            product.addProperty("address",edtAddress.text.toString())
            product.addProperty("phone",edtPhone.text.toString())
            product.addProperty("phone2",edtPhone2.text.toString())
            product.addProperty("provinceId",1)
            product.addProperty("districtId",1)
            product.addProperty("leaderId",leaderId)
            product.add("memberIds",workerIds)
            createProduct(product)
        }

        tvChooseLeader.setOnClickListener {
            val intent = Intent(this, ChooseTeamLeaderActivity::class.java)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        imvAva.setOnClickListener {
            val intent = Intent(this, ChooseTeamLeaderActivity::class.java)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        tvChooseWorker.setOnClickListener {
            val intent = Intent(this, ChooseTeamWorkerActivity::class.java)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        imvAvaWorker.setOnClickListener {
            val intent = Intent(this, ChooseTeamWorkerActivity::class.java)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        setupRecyclerView(myList)
    }

    override fun resumeData() {

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
                if( response!!.body() != null && response.body().status == 1){
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

    var myList = ArrayList<Worker2>()
    private fun getTeamLeader(){
        showProgessDialog()
        RestClient().getInstance().getRestService().getWorkers2(0,"").enqueue(object :
            Callback<RestData<ArrayList<Worker2>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Worker2>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<Worker2>>>?, response: Response<RestData<ArrayList<Worker2>>>?) {
                dismisProgressDialog()
                if( response!!.body().status == 1){
                    myList = response.body().data!!

                }
            }
        })
    }

    private fun setupRecyclerView(data:MutableList<Worker2>){
        mAdapter = LeaderHoriAdapter(data)
        recyclerViewTeamLeader.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL ,false)
        recyclerViewTeamLeader.setHasFixedSize(false)
        recyclerViewTeamLeader.adapter = mAdapter

        mAdapter!!.onItemClick = { position ->
            myList.removeAt(position)
//            mAdapter.notifyDataSetChanged()
            mAdapter!!.notifyItemRemoved(position)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode ==  100){
            val avatarUrl = data!!.getStringExtra("avatarUrl")
            leaderId = data.getIntExtra("id", 0)
            if (avatarUrl != null) {
                Picasso.get().load(avatarUrl).into(imvAva)
            } else {
                imvAva.setImageResource(R.drawable.ava);
            }
        } else if (resultCode == 101){
//            val arrayParents: ArrayList<Worker2> = intent.getParcelableArrayListExtra("workersChoose")
//            val arrayParents = data!!.getParcelableArrayListExtra<Worker2>("workersChoose")
//            setupRecyclerView( ConstantsApp.workers)
            myList.clear()
            myList.addAll(ConstantsApp.workers)
            mAdapter!!.notifyDataSetChanged()
            imvAvaWorker.visibility = View.GONE
            setupRecyclerView(myList)
            myList.forEach {
                workerIds.add(it.id)
            }
        }
    }

}
