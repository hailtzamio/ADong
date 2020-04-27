package com.zamio.adong.ui.workoutline

import RestClient
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.R
import com.zamio.adong.model.WorkOutline
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_update_workeoutline.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateWorkOutlineActivity : BaseActivity() {

    val worker = JsonObject()
    override fun getLayout(): Int {
       return R.layout.activity_update_workeoutline
    }

    override fun initView() {
        rightButton.visibility = View.GONE
        tvTitle.text = "Cập Nhật"
    }

    override fun initData() {
        val productOb = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as WorkOutline

        edtName.setText(productOb.name)
        edtSequence.setText(productOb.sequence.toString())

        tvOk.setOnClickListener {

            if(isEmpty(edtName) || isEmpty(edtSequence)){
                showToast("Nhập thiếu thông tin")
                return@setOnClickListener
            }

            worker.addProperty("name",edtName.text.toString())
            worker.addProperty("sequence",edtSequence.text.toString())
            update(productOb.id,worker )
        }

    }

    private fun update(id:Int, woker: JsonObject){

        showProgessDialog()
        RestClient().getInstance().getRestService().updateWorkOutline(id,woker).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response!!.body().status == 1){
                    showToast("Cập nhật thành công")
                    setResult(100)
                    finish()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }

    override fun resumeData() {

    }
}
