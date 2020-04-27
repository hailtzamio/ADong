package com.zamio.adong.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_preview_image.*

class PreviewImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_image)
        val avatarUrl = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as String
        Picasso.get().load(avatarUrl).into(imvAva)

        imvClose.setOnClickListener {
            finish()
        }
    }
}
