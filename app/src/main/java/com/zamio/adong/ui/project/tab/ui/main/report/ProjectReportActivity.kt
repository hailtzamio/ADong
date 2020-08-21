package com.zamio.adong.ui.project.tab.ui.main.report

import RestClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.model.Information
import com.zamio.adong.model.Project
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.utils.Utils
import kotlinx.android.synthetic.main.activity_project_report.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectReportActivity : BaseActivity() {

    var id = 0
    override fun getLayout(): Int {
        return R.layout.activity_project_report
    }

    override fun initView() {
            tvTitle.text = "Báo Cáo"
        rightButton.visibility = View.GONE
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)

            getData(id)
            setupChooseSpinner()
        }
    }

    override fun resumeData() {

    }

    var title = "Chọn người đi mua?"
    private fun setupChooseSpinner() {
        val list: MutableList<String> = ArrayList()
        list.add("Đang làm HSQT")
        list.add("Đã gửi chủ đầu tư")
        list.add("Đã xác nhận thông tin XHĐ")
        list.add("Đã thanh toán")

        val dataAdapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item, list
        )
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinType.adapter = dataAdapter
        spinType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> title = "Chọn người đi mua?"
                    1 -> title = "Chọn xưởng sản xuất?"
                    2 -> title = "Chọn kho?"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    fun getData(id: Int) {
        RestClient().getInstance().getRestService().getProject(id).enqueue(object :
            Callback<RestData<Project>> {

            override fun onFailure(call: Call<RestData<Project>>?, t: Throwable?) {

            }

            override fun onResponse(
                call: Call<RestData<Project>>?,
                response: Response<RestData<Project>>?
            ) {
                if (response!!.body() != null && response.body().status == 1 && response.body().data != null) {

                    when(response.body().data!!.accountingStatus) {
                        1 -> tv1.text = "Đã bàn giao, đang xử lý hồ sơ quyết toán"
                        2 -> tv1.text = "Đã gửi hồ sơ quyết toán, chờ chủ đầu tư phản hồi"
                        3 -> tv1.text = "Đã xác nhận xuất hóa đơn, chuyển thông tin kế toán"
                        4 -> tv1.text = "Đã xuất hóa đơn gửi chủ đầu tư, chờ thanh toán"
                        5 -> tv1.text = "Đã thanh toán"
                    }
                }
            }
        })
    }
}