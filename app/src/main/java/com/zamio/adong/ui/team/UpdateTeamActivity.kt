package com.zamio.adong.ui.team

import MemberTeamAdapter
import RestClient
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.adapter.CustomAdapter
import com.zamio.adong.model.Province
import com.zamio.adong.model.Team
import com.zamio.adong.model.Worker
import com.zamio.adong.model.Worker2
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_create_team.*
import kotlinx.android.synthetic.main.activity_update_lorry.tvOk
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateTeamActivity : BaseActivity() {

    var mAdapter: MemberTeamAdapter? = null
    var leaderId = 0
    var teamId = 0
    var workerIds = JsonArray()
    var myList = ArrayList<Worker2>()
    var provinces = ArrayList<Province>()
    var districts = ArrayList<Province>()
    var provinceId = 1
    var districtId = 1
    override fun getLayout(): Int {
        return R.layout.activity_update_team
    }

    override fun initView() {
        tvTitle.text = "Cập Nhật"
        rightButton.visibility = View.GONE
    }

    override fun initData() {

        ConstantsApp.workers.clear()
        val team = intent.extras!!.get(ConstantsApp.KEY_QUESTION_ID) as Team

        edtName.setText(team.name)
        edtName.setTextColor(ContextCompat.getColor(this,R.color.colorName))
        edtAddress.setText(team.address)
        edtAddress.setTextColor(ContextCompat.getColor(this,R.color.colorName))
        edtPhone.text = team.phone
        edtPhone2.setText(team.phone2)
        edtPhone2.setTextColor(ContextCompat.getColor(this,R.color.colorName))
        leaderId = team.leaderId
        teamId = team.id

        provinceId = team.provinceId
        districtId = team.districtId

        getProvinces()
        getDistricts(provinceId)
        getLeader(leaderId)
        getWorkerFromTeamId(team.id)

        tvOk.setOnClickListener {

            workerIds = JsonArray()
            myList.forEach {
                workerIds.add(it.id)
            }

            if (isEmpty(edtName) || isEmpty(edtAddress)) {
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            if (leaderId == 0) {
                showToast("Chọn Đội trưởng")
                return@setOnClickListener
            }

            if (workerIds.count() == 0) {
                showToast("Chọn Công nhân")
                return@setOnClickListener
            }

            val product = JsonObject()
            product.addProperty("name", edtName.text.toString())
            product.addProperty("address", edtAddress.text.toString())
            product.addProperty("phone", edtPhone.text.toString())
            product.addProperty("phone2", edtPhone2.text.toString())
            product.addProperty("provinceId", provinceId)
            product.addProperty("districtId", districtId)
            product.addProperty("leaderId", leaderId)
            product.add("memberIds", workerIds)
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

    private fun createProduct(data: JsonObject) {
        showProgessDialog()
        RestClient().getInstance().getRestService().updateTeam(teamId, data).enqueue(object :
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
                    showToast("Cập nhật thành công")
                    setResult(100)
                    finish()
                } else {
                    val obj = JSONObject(response.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }

    private fun getProvinces() {
        showProgessDialog()
        RestClient().getInstance().getRestService().getProvince().enqueue(object :
            Callback<RestData<ArrayList<Province>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Province>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<ArrayList<Province>>>?,
                response: Response<RestData<ArrayList<Province>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    provinces = response.body().data!!
                    setupProvinceSpinner()
                }
            }
        })
    }

    private fun getDistricts(provinceId: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getDistricts(provinceId).enqueue(object :
            Callback<RestData<ArrayList<Province>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Province>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<ArrayList<Province>>>?,
                response: Response<RestData<ArrayList<Province>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    districts = response.body().data!!
                    setupDistrictSpinner()
                }
            }
        })
    }

    private fun getLeader(id:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getWorker(id).enqueue(object :
            Callback<RestData<Worker>> {

            override fun onFailure(call: Call<RestData<Worker>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<Worker>>?, response: Response<RestData<Worker>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response!!.body().status == 1){
                    val product = response.body().data ?: return
                    tvLeaderName.text = product.fullName
                    Picasso.get().load(product.avatarUrl).into(imvAva)
                }
            }
        })
    }

    private fun getWorkerFromTeamId(teamId: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getWorkerFromTeam(teamId).enqueue(object :
            Callback<RestData<ArrayList<Worker2>>> {

            override fun onFailure(call: Call<RestData<ArrayList<Worker2>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<ArrayList<Worker2>>>?,
                response: Response<RestData<ArrayList<Worker2>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    myList = response.body().data!!
                    setupRecyclerView(myList)
                    myList.forEach {
                        workerIds.add(it.id)
                    }

                    ConstantsApp.workers = myList
                }
            }
        })
    }

    private fun setupRecyclerView(data: MutableList<Worker2>) {
        mAdapter = MemberTeamAdapter(data, true)
        recyclerViewTeamLeader.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewTeamLeader.setHasFixedSize(false)
        recyclerViewTeamLeader.isNestedScrollingEnabled = true
        recyclerViewTeamLeader.adapter = mAdapter

        mAdapter!!.onItemClick = { position ->
            myList.removeAt(position)
            mAdapter!!.notifyItemRemoved(position)
            checkShowOrHideButton()
        }
    }

    private fun setupProvinceSpinner() {
        var selection = 0
        for (i in 0 until provinces.size) {
            if (provinces[i].id == provinceId) {
                selection = i
            }
        }

        val mAdapter = CustomAdapter(this, R.layout.item_provice_spinner, R.id.tvName, provinces)
        mAdapter.setDropDownViewResource(R.layout.item_provice_spinner)
        spinProvince.adapter = mAdapter
        spinProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                provinceId = provinces[position].id
                getDistricts(provinceId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinProvince.setSelection(selection)
    }

    private fun setupDistrictSpinner() {

        var selection = 0
        for (i in 0 until districts.size) {
            if (districts[i].id == districtId) {
                selection = i
            }
        }

        val mAdapter = CustomAdapter(this, R.layout.item_provice_spinner, R.id.tvName, districts)
        mAdapter.setDropDownViewResource(R.layout.item_provice_spinner)
        spinDistrict.adapter = mAdapter
        spinDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                districtId = districts[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        spinDistrict.setSelection(selection)
    }

    private fun checkShowOrHideButton() {
        if (myList.size == 0) {
            imvAvaWorker.visibility = View.VISIBLE
        } else {
            imvAvaWorker.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 100) {

            if (data == null) return
            val avatarUrl = data!!.getStringExtra("avatarUrl")
            leaderId = data.getIntExtra("id", 0)
            edtPhone.setText(data.getStringExtra("phone").toString())

            if (avatarUrl != null) {
                Picasso.get().load(avatarUrl).into(imvAva)
            } else {
                imvAva.setImageResource(R.drawable.ava);
            }

        } else if (resultCode == 101) {
            myList.clear()
            myList.addAll(ConstantsApp.workers)
            mAdapter!!.notifyDataSetChanged()
            imvAvaWorker.visibility = View.GONE
            setupRecyclerView(myList)
            checkShowOrHideButton()
        }
    }

}
