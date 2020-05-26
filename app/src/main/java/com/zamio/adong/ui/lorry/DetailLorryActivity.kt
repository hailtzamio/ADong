package com.zamio.adong.ui.lorry

import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.Lorry
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_detail_lorry.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailLorryActivity : BaseActivity(), OnMapReadyCallback {

    var id = 1
    var model:Lorry? = null

    val ZOOM_LEVEL = 5f
    var lat = 0.0
    var lg = 0.0
    var mGoogleMap:GoogleMap? = null
    override fun getLayout(): Int {
        return R.layout.activity_detail_lorry
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.setImageResource(R.drawable.icon_update);

        if(!ConstantsApp.PERMISSION.contains("u")){
            rightButton.visibility = View.GONE
        }

        if(!ConstantsApp.PERMISSION.contains("d")){
            tvOk.visibility = View.GONE
        }

        val mapFragment : SupportMapFragment? =
            supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap ?: return
        mGoogleMap = googleMap

    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)){

             id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)

            tvOk.setOnClickListener {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                removeLorry()
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage("Xóa xe này?").setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show()
            }

            rightButton.setOnClickListener {
                val intent = Intent(this, UpdateLorryActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, model!!)
                startActivity(intent)
                this!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    override fun resumeData() {
        getLorry(id)
    }

    private fun getLorry(id:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getLorry(id).enqueue(object :
            Callback<RestData<Lorry>> {

            override fun onFailure(call: Call<RestData<Lorry>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<Lorry>>?, response: Response<RestData<Lorry>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response!!.body().status == 1){
                    model = response.body().data ?: return
                    if(model != null) {
                        tvName.text = model!!.brand
                        tvModel.text = model!!.model
                        tvPlateNumber.text = model!!.plateNumber
                        tvCapacity.text = model!!.capacity
                        lat = model!!.latitude
                        lg = model!!.longitude

                        if( mGoogleMap != null) {
                            val location = LatLng(lat, lg)
                            with(mGoogleMap!!) {
                                moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(location, ZOOM_LEVEL))
                                addMarker(com.google.android.gms.maps.model.MarkerOptions().position(location))
                            }
                        }
                    }
                }
            }
        })
    }

    private fun removeLorry(){

        showProgessDialog()
        RestClient().getRestService().removeLorry(id).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if( response!!.body().status == 1){
                    showToast("Xóa thành công")
                    setResult(100)
                    finish()
                }
            }
        })
    }

}
