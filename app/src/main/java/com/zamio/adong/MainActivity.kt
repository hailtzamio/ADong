package com.zamio.adong

import RestClient
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.utils.PreferUtils
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {
    override fun getLayout(): Int {

    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun resumeData() {
      
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        val preferUtils = PreferUtils()
        val userId = preferUtils.getUserId(this)

        if(!userId.equals("product")){
            setContentView(R.layout.activity_main)
        } else {
            setContentView(R.layout.activity_product)
        }

//        setContentView(R.layout.activity_product)


        val navView: BottomNavigationView = this.findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


}
