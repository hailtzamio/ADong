package com.zamio.adong.ui.activity

import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_preview_image.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreviewImageActivity : BaseActivity() {

    var id = 0
    override fun getLayout(): Int {
        return R.layout.activity_preview_image
    }

    override fun initView() {

    }

    override fun initData() {
        var avatarUrl = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as String
        if(intent.hasExtra(ConstantsApp.KEY_VALUES_HIDE)) {
            avatarUrl = avatarUrl + "&accessToken=" + ConstantsApp.BASE64_AUTH_TOKEN.removeRange(0,6)
        }

        if(intent.hasExtra(ConstantsApp.KEY_VALUES_OBJECT)) {
            id = intent.extras!!.getInt(ConstantsApp.KEY_VALUES_OBJECT)
            imvRemove.visibility = View.VISIBLE
        }

        if(intent.hasExtra(ConstantsApp.KEY_VALUES_TITLE)) {
            tvTitle.text = intent.extras!!.getString(ConstantsApp.KEY_VALUES_TITLE)
        }

        Picasso.get().load(avatarUrl).into(imvAva)

        imvClose.setOnClickListener {
            finish()
        }

        imvRemove.setOnClickListener {
            val dialogClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            removeWorkOutlineCompletionPhoto()
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Xóa ảnh này?").setPositiveButton("Đồng ý", dialogClickListener)
                .setNegativeButton("Không", dialogClickListener).show()
        }
    }

    override fun resumeData() {

    }

    private fun removeWorkOutlineCompletionPhoto(){

        showProgessDialog()
        RestClient().getRestService().removeWorkOutlineCompletionPhoto(id).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if( response!!.body() != null && response.body().status == 1){
                    showToast("Xóa thành công")
                    setResult(100)
                    finish()
                } else {
                    showToast("Không thành công")
                }
            }
        })
    }
}
