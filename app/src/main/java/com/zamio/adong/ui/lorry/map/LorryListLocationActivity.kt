package com.zamio.adong.ui.lorry.map

import RestClient
import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.zamio.adong.R
import com.zamio.adong.model.Lorry
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_lorry_list_location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class LorryListLocationActivity : FragmentActivity(), OnMapReadyCallback, LocationListener {


    val ZOOM_LEVEL = 5f
    var locationManager: LocationManager? = null
    var latitude = 18.787203
    var longitude = 105.605202
    var mGoogleMap:GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lorry_list_location)
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

    private fun getProducts() {

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



    val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val latitude = location.latitude
            val longitude = location.longitude
            val msg = "New Latitude: " + latitude + "New Longitude: " + longitude
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
            addMarker(com.google.android.gms.maps.model.MarkerOptions().position(location))
        }
    }

    override fun onLocationChanged(map: Location?) {

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