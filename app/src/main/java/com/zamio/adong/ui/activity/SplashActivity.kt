package com.zamio.adong.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elcom.com.quizupapp.utils.PreferUtils
import com.zamio.adong.MainActivity
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val preferUtils = PreferUtils()
        val token = preferUtils.getToken(this)

        if(token != "") {
            ConstantsApp.BASE64_AUTH_TOKEN = token
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
