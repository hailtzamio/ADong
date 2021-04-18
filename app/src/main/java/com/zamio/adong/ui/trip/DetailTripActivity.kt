package com.zamio.adong.ui.trip

import InformationAdapter
import RestClient
import TransportAdapter
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.ui.network.UserRoles
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.zamio.adong.R
import com.zamio.adong.model.Information
import com.zamio.adong.model.Transport
import com.zamio.adong.model.Trip
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.utils.Utils
import kotlinx.android.synthetic.main.activity_transport_detail.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetailTripActivity : BaseActivity() {


    var model: Trip? = null
    var id = 1
    var status = 0
    override fun getLayout(): Int {
        return R.layout.activity_transport_detail
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);
        topTitle.text = "DANH SÁCH YCVC"
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {

            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)


            if (!ConstantsApp.PERMISSION.contains("u")) {
                rightButton.visibility = View.GONE
            }

//            if (!ConstantsApp.PERMISSION.contains("d")) {
//                tvOk.visibility = View.GONE
//            }
            rightButton.setImageResource(R.drawable.ava);
            rightButton.setOnClickListener {
                val intent = Intent(this, TripAlbumImage::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, id)
                startActivityForResult(intent, 1000)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            tvOk.setOnClickListener {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
            }

        }
    }

    override fun resumeData() {
        getData(id)
    }

    val mList = ArrayList<Information>()
    val mListProduct = ArrayList<Information>()
    private fun getData(id: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getTrip(id).enqueue(object :
            Callback<RestData<Trip>> {

            override fun onFailure(call: Call<RestData<Trip>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<Trip>>?,
                response: Response<RestData<Trip>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    model = response.body().data ?: return
                    status = model!!.status ?: 1

                    mList.clear()

                    mList.add(Information("Code", model!!.code ?: "---", ""))

//                    if(model!!.status ?: 1 == 3) {
//                        mList.add(Information("Trạng thái", "Hoàn thành", ""))
//                    } else if(model!!.status ?: 1 == 2) {
//                        mList.add(Information("Trạng thái", "Đã Hủy", ""))
//                    } else {
//                        mList.add(Information("Trạng thái", "Mới", ""))
//                    }

                    mList.add(Information("Lái xe", model!!.driverFullName ?: "---", ""))
                    mList.add(Information("Số điện thoại", model!!.driverPhone ?: "---", ""))
                    mList.add(Information("Biến số xe", model!!.lorryPlateNumber ?: "---", ""))
                    mList.add(Information("Ngày dự kiến", Utils.convertDate(model!!.plannedDatetime ?: "2020-20-20T11:11:11"), ""))

                    setupRecyclerView(mList)

                    if(model!!.transportRequests != null) {

                        val transport = model!!.transportRequests

                        transport!!.forEach {
                            it.plannedDatetime = it.projectName
                            it.projectName = it.warehouseAddress
                            it.note = it.projectAddress
                        }

                        setupRecyclerViewSmall(model!!.transportRequests!!)
                    }
                }
            }
        })
    }

    private fun setupRecyclerView(data: ArrayList<Information>) {
        val mAdapter = InformationAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter
    }

    private fun setupRecyclerViewSmall(data: ArrayList<Transport>) {
        val mAdapter = TransportAdapter(data)
        recyclerView2.layoutManager = LinearLayoutManager(this)
        recyclerView2.setHasFixedSize(false)
        recyclerView2.adapter = mAdapter

        mAdapter.onItemClick = {
            val intent = Intent(this, DetailTransportActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, it.id)
            startActivityForResult(intent,1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri: Uri = result.uri
                val file = File(resultUri.path!!)


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

}
