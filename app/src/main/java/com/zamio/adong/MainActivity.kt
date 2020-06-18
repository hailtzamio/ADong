package com.zamio.adong

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.RECORD_AUDIO
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.utils.PreferUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity() {


    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun getLayout(): Int {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        val preferUtils = PreferUtils()
        val userId = preferUtils.getUserId(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        return R.layout.activity_main
    }

    override fun initView() {
        val navView: BottomNavigationView = this.findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    override fun initData() {
        if (!checkPermissions()) {
            requestPermissions()
        }
//        requestPermissions()
    }

    override fun resumeData() {
      
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        val preferUtils = PreferUtils()
//        val userId = preferUtils.getUserId(this)
//
//        if(!userId.equals("product")){
//            setContentView(R.layout.activity_main)
//        } else {
//            setContentView(R.layout.activity_product)
//        }
//
////        setContentView(R.layout.activity_product)
//
//
//        val navView: BottomNavigationView = this.findViewById(R.id.nav_view)
//
//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
////        val appBarConfiguration = AppBarConfiguration(setOf(
////                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
////        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
    }



    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}
