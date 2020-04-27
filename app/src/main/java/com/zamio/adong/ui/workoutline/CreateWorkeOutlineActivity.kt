package com.zamio.adong.ui.workoutline

import RestClient
import android.util.Patterns
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.R
import kotlinx.android.synthetic.main.activity_update_workeoutline.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateWorkeOutlineActivity : BaseActivity() {


    val product = JsonObject()
    override fun getLayout(): Int {
        return R.layout.activity_update_workoutline
    }

    override fun initView() {
        tvTitle.text = "Tạo Hạng Mục"
        rightButton.visibility = View.GONE
    }

    override fun initData() {

        tvOk.setOnClickListener {

            if (isEmpty(edtName) || isEmpty(edtSequence)) {
            showToast("Nhập thiếu thông tin")
            return@setOnClickListener
            }

            product.addProperty("name", edtName.text.toString())
            product.addProperty("sequence", edtSequence.text.toString())

            create(product)
        }

    }

    fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    override fun resumeData() {

    }

    private fun create(product: JsonObject) {
        showProgessDialog()
        RestClient().getInstance().getRestService().createWorkOutline(product).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response?.body() != null && response.body().status == 1) {
                    showToast("Tạo thành công")
                    setResult(100)
                    finish()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }
}
