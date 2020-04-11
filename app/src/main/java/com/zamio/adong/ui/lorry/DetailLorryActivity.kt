package com.zamio.adong.ui.lorry

import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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

    var lorryId = 1
    var lorry:Lorry? = null
    val SYDNEY = LatLng(10.762622, 106.660172)
    val ZOOM_LEVEL = 5f
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
        with(googleMap) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, ZOOM_LEVEL))
            addMarker(MarkerOptions().position(SYDNEY))
        }
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_QUESTION_ID)){

             lorryId = intent.getIntExtra(ConstantsApp.KEY_QUESTION_ID, 1)

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
                intent.putExtra(ConstantsApp.KEY_QUESTION_ID, lorry!!)
                startActivity(intent)
                this!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    override fun resumeData() {
        getLorry(lorryId)
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
                    lorry = response.body().data ?: return
                    if(lorry != null) {
                        tvName.text = lorry!!.brand
                        tvModel.text = lorry!!.model
                        tvPlateNumber.text = lorry!!.plateNumber
                        tvCapacity.text = lorry!!.capacity
                    }
                }
            }
        })
    }

    private fun removeLorry(){

        showProgessDialog()
        RestClient().getRestService().removeLorry(lorryId).enqueue(object :
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
