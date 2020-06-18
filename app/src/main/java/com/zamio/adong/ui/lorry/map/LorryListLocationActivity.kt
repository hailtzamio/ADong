package com.zamio.adong.ui.lorry.map

import RestClient
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.zamio.adong.R
import com.zamio.adong.model.Lorry
import com.zamio.adong.model.Project
import com.zamio.adong.network.ConstantsApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LorryListLocationActivity : BaseActivity(), OnMapReadyCallback, LocationListener {


    val ZOOM_LEVEL = 5f
    var locationManager: LocationManager? = null
    var latitude = 18.787203
    var longitude = 105.605202
    var mGoogleMap:GoogleMap? = null
    override fun getLayout(): Int {
        return R.layout.activity_lorry_list_location
    }

    override fun initView() {

    }

    override fun initData() {
        val mapFragment : SupportMapFragment? =
            supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
    }

    override fun resumeData() {

    }

    private fun getLorries() {

        RestClient().getInstance().getRestService().getLorries().enqueue(object :
            Callback<RestData<List<Lorry>>> {
            override fun onFailure(call: Call<RestData<List<Lorry>>>?, t: Throwable?) {

            }

            override fun onResponse(
                call: Call<RestData<List<Lorry>>>?,
                response: Response<RestData<List<Lorry>>>?
            ) {

                if (response!!.body() != null && response.body().status == 1) {
                        val mList = response.body().data
                        if(mList != null) {
                            mList.forEach {
                                val location = LatLng(it.latitude, it.longitude)
                                with(mGoogleMap!!) {
                                    addMarker(com.google.android.gms.maps.model.MarkerOptions().position(location))
                                }
                            }
                        }
                }
            }
        })
    }

    private fun getBitmapIcon() : Bitmap {
        val height = 100
        val width = 100
        val bitmapdraw =
            ResourcesCompat.getDrawable(resources,R.drawable.marker_01, null) as BitmapDrawable
        val b = bitmapdraw.bitmap
        return Bitmap.createScaledBitmap(b, width, height, false)
    }

    private fun getProjects() {
        showProgessDialog()
        RestClient().getInstance().getRestService().getProjects(0, "", "")
            .enqueue(object :
                Callback<RestData<List<Project>>> {
                override fun onFailure(call: Call<RestData<List<Project>>>?, t: Throwable?) {
                    dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<List<Project>>>?,
                    response: Response<RestData<List<Project>>>?
                ) {
                    dismisProgressDialog()
                    if (response!!.body() != null && response!!.body().status == 1) {
                        val mList = response.body().data
                        if(mList != null) {
                            mList.forEach {
                                val location = LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0)
                                with(mGoogleMap!!) {
                                    if (it.status == "PROCESSING") {
                                        addMarker(com.google.android.gms.maps.model.MarkerOptions().position(location).title(it.name ?: "").icon(
                                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                                    } else if(it.status == "NEW") {
                                        addMarker(com.google.android.gms.maps.model.MarkerOptions().position(location).title(it.name ?: "").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                                    } else {
                                        addMarker(com.google.android.gms.maps.model.MarkerOptions().position(location).title(it.name ?: "").icon(BitmapDescriptorFactory.fromBitmap(getBitmapIcon())))
                                    }

                                }
                            }
                        }
                    }
                }
            })
    }



    val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
//            val latitude = location.latitude
//            val longitude = location.longitude
//            val msg = "New Latitude: " + latitude + "New Longitude: " + longitude
//            Log.d("", "$msg")
//            Toast.makeText(this@LorryLocationActivity, msg, Toast.LENGTH_LONG).show()
        }

        override fun onStatusChanged(
            provider: String,
            status: Int,
            extras: Bundle
        ) {
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap ?: return
        mGoogleMap = googleMap
        val location = LatLng(10.810583, 106.709145)
        with(mGoogleMap!!) {
            moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(location, ZOOM_LEVEL))
//            addMarker(com.google.android.gms.maps.model.MarkerOptions().position(location))
        }
        if(intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            getProjects()
        } else {
            getLorries()
        }

    }

    override fun onLocationChanged(map: Location?) {
//        val latitude = map!!.latitude ?: 0.0
//        val longitude = map!!.longitude
//        val msg = "New Latitude: " + latitude + "New Longitude: " + longitude
//        Log.d("", "$msg")
//        Toast.makeText(
//            this,
//            "Lat " + map!!.latitude.toString() + " " + "Long " + map!!.longitude.toString(),
//            Toast.LENGTH_LONG
//        ).show()
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onProviderEnabled(p0: String?) {

    }

    override fun onProviderDisabled(p0: String?) {

    }

    override fun onDestroy() {
        super.onDestroy()

    }


}