package com.zamio.adong.ui.project.tab.ui.main.album

import RestClient
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.adapter.GridProjectImageAdapter
import com.zamio.adong.model.ProjectImage
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.activity.PreviewImageActivity
import kotlinx.android.synthetic.main.activity_checkin_out_album_image.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishProjectAlbumActivity : BaseActivity() {

    var id = 0
    private var itemList:List<ProjectImage>? = null

    override fun getLayout(): Int {
        return R.layout.activity_checkin_out_album_image
    }

    override fun initView() {
        tvTitle.text = "Ảnh Nghiệm Thu"
        rightButton.visibility = View.GONE
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)
            getData()
        }
    }

    private fun setupView() {
        val gridview = findViewById<GridView>(R.id.gridview)
        val adapter = GridProjectImageAdapter(this, R.layout.item_project_image, itemList)
        gridview.adapter = adapter

        gridview.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->

            if (itemList!![position].fullSizeUrl != null) {
                val intent = Intent(this, PreviewImageActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, itemList!!.get(position).fullSizeUrl)
                startActivityForResult(intent, 1000)
            }
        }
    }

    override fun resumeData() {

    }

    private fun getData() {
        showProgessDialog()
        RestClient().getInstance().getRestService().getProjectFinishImages(id).enqueue(object :
            Callback<RestData<List<ProjectImage>>> {
            override fun onFailure(call: Call<RestData<List<ProjectImage>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<List<ProjectImage>>>?,
                response: Response<RestData<List<ProjectImage>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    itemList = response.body().data
                    if (itemList!!.isNotEmpty()) {
                        viewNoData.visibility = View.GONE
                        setupView()
                    } else {
                        viewNoData.visibility = View.VISIBLE
                    }

                }
            }
        })
    }
}
