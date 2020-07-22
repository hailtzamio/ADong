package com.zamio.adong.ui.activity

import android.Manifest
import android.R.id
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.elcom.com.quizupapp.utils.PreferUtils
import com.zamio.adong.MainActivity
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import java.io.*


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

//        writeFile()
//        writeFile1("Ahihi")

//        val myDir = File(cacheDir, "ADong")
//        myDir.mkdir()
//
//        val direct =
//            File(Environment.getExternalStorageDirectory().toString() + "/ADongg")
//
//        if (!direct.exists()) {
//            if (direct.mkdir());
//        }
    }

//    fun writeFile1(value: String?) {
//        val PATH = "/ADong"
//        val directoryName = PATH + javaClass.name
//        val fileName: String = "hhh.txt"
//        val directory = File(directoryName)
//        if (!directory.exists()) {
//            directory.mkdir()
//            // If you require it to make the entire directory path including parents,
//            // use directory.mkdirs(); here instead.
//        }
//        val file = File("$directoryName/$fileName")
//        try {
//            val fw = FileWriter(file.absoluteFile)
//            val bw = BufferedWriter(fw)
//            bw.write(value)
//            bw.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//            System.exit(-1)
//        }
//    }
//
//    fun writeFile() {
//
//        var filename = "blesson.txt"
//        // create a File object for the parent directory
//        val wallpaperDirectory = File("/sdcard/Adong/")
//        // have the object build the directory structure, if needed.
//        wallpaperDirectory.mkdirs()
//        // create a File object for the output file
//        val outputFile = File(wallpaperDirectory, filename)
//        // now attach the OutputStream to the file object, instead of a String representation
//        try {
//            val fos = FileOutputStream(outputFile)
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun isStoragePermissionGranted(): Boolean {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED) {
//                return true
//            } else {
//
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
//                return false
//            }
//        } else { //permission is automatically granted on sdk<23 upon installation
//            return true
//        }
//    }


}


