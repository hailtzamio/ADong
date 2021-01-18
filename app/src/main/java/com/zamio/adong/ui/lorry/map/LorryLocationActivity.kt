package com.zamio.adong.ui.lorry.map

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
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_lorry_location.*
import java.io.IOException
import java.util.*


class LorryLocationActivity : FragmentActivity(), OnMapReadyCallback, LocationListener {


    val ZOOM_LEVEL = 16f
    var locationManager: LocationManager? = null
    var latitude = 18.787203
    var longitude = 105.605202
    var mGoogleMap:GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lorry_location)
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)


        rlAddress.setOnClickListener {
            onBackPressed()
        }
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

        if (intent.hasExtra(ConstantsApp.KEY_VALUES_LAT)) {
            latitude = intent.getDoubleExtra(ConstantsApp.KEY_VALUES_LAT, 0.0)
            longitude = intent.getDoubleExtra(ConstantsApp.KEY_VALUES_LONG, 0.0)
            if(mGoogleMap != null) {
                val location = LatLng(latitude, longitude)
                with(mGoogleMap!!) {
                    moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(location, ZOOM_LEVEL))
                    addMarker(com.google.android.gms.maps.model.MarkerOptions().position(location))
                }

                val geocoder: Geocoder
                val addresses: List<Address>
                geocoder = Geocoder(this, Locale.getDefault())

                addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1
                ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                if(addresses.isNotEmpty()) {
                    val address = addresses[0]
                        .getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                    val city = addresses[0].locality
                    val state = addresses[0].adminArea
                    val country = addresses[0].countryName
                    val postalCode = addresses[0].postalCode
                    val knownName = addresses[0].featureName

                    adressText.text = address
                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {

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