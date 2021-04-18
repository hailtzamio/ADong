package com.zamio.adong.ui.project.tab.ui.main.requirement

import InformationAdapter
import RestClient
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.ui.network.UserRoles
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.zamio.adong.R
import com.zamio.adong.model.*
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.utils.Utils
import kotlinx.android.synthetic.main.activity_transport_detail.recyclerView
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductTransportDetailActivity : BaseActivity(), OnMapReadyCallback {

    val ZOOM_LEVEL = 5f
    var lat = 0.0
    var lg = 0.0
    var mGoogleMap: GoogleMap? = null
    var model: Lorry? = null
    var productRequirement: ProductRequirement? = null
    val mList = ArrayList<Information>()
    override fun getLayout(): Int {
        return R.layout.activity_product_transport_detail
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.visibility = View.GONE
    }

    override fun initData() {
        productRequirement =
            intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as ProductRequirement

        if (productRequirement != null && productRequirement!!.transportReqId != null) {
            getTransport(productRequirement!!.transportReqId!!)



        }
        val mapFragment: SupportMapFragment? =
            supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    private fun setupRecyclerView(data: ArrayList<Information>) {
        val mAdapter = InformationAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter
    }

    override fun resumeData() {

    }

    private fun getTransport(id: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getTransport(id).enqueue(object :
            Callback<RestData<Transport>> {

            override fun onFailure(call: Call<RestData<Transport>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<Transport>>?,
                response: Response<RestData<Transport>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    val transport = response.body().data
                    if (response.body().data != null) {
                        getTrip(response.body().data!!.tripId ?: 0)


                        mList.add(Information("Điểm đi", transport!!.warehouseAddress ?: "---", ""))
                        mList.add(Information("Điểm đến", transport!!.projectAddress ?: "---", ""))
//                        mList.add(
//                            Information(
//                                "Ngày dự kiến",
//                                Utils.convertDate(productRequirement!!.plannedDatetime ?: "2020-07-28T10:12:29"),
//                                ""
//                            )
//                        )
                    }
                }
            }
        })
    }

    private fun getTrip(id: Int) {
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
                    val data = response.body().data
                    mList.add(Information("Tài xế", data!!.driverFullName ?: "---", ""))
                    mList.add(Information("Số điện thoại", data!!.driverPhone ?: "---", ""))
                    if (response.body().data != null) {
                        getLorry(response.body().data!!.lorryId ?: 0)
                    }
                }
            }
        })
    }

    private fun getLorry(id: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getLorry(id).enqueue(object :
            Callback<RestData<Lorry>> {

            override fun onFailure(call: Call<RestData<Lorry>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<Lorry>>?,
                response: Response<RestData<Lorry>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    model = response.body().data ?: return
                    if (model != null) {

                        mList.add(Information("Biển số xe", model!!.plateNumber ?: "---", ""))
                        setupRecyclerView(mList)

                        lat = model!!.latitude
                        lg = model!!.longitude

                        if (mGoogleMap != null) {
                            val location = LatLng(lat, lg)
                            with(mGoogleMap!!) {
                                moveCamera(
                                    com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                                        location,
                                        ZOOM_LEVEL
                                    )
                                )
                                addMarker(
                                    com.google.android.gms.maps.model.MarkerOptions()
                                        .position(location)
                                )
                            }
                        }
                    }
                }
            }
        })

    }


    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap ?: return
        mGoogleMap = googleMap

        if (mGoogleMap != null) {
            val location = LatLng(lat, lg)
            with(mGoogleMap!!) {
                moveCamera(
                    com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                        location,
                        ZOOM_LEVEL
                    )
                )
                addMarker(com.google.android.gms.maps.model.MarkerOptions().position(location))
            }
        }
    }
}