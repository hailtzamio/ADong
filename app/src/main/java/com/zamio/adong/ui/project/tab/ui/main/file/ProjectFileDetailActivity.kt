package com.zamio.adong.ui.project.tab.ui.main.file

import RestClient
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.adapter.GridProjectFileAdapter
import com.zamio.adong.adapter.GridProjectFileDetailAdapter
import com.zamio.adong.model.FileProject
import com.zamio.adong.model.Product
import com.zamio.adong.model.Project
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_checkin_out_album_image.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectFileDetailActivity : BaseActivity() {

    var id = 0
    private var itemList = ArrayList<FileProject>()

    override fun getLayout(): Int {
        return R.layout.activity_checkin_out_album_image
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.visibility = View.GONE
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            val productOb = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as Project
            if(productOb.designFiles != null) {
                itemList = productOb.designFiles ?: ArrayList<FileProject>()

                if (itemList.isNotEmpty()) {
                    viewNoData.visibility = View.GONE
                    setupView()
                } else {
                    viewNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupView() {
        val gridview = findViewById<GridView>(R.id.gridview)
        val adapter = GridProjectFileDetailAdapter(this, R.layout.item_project_file, itemList)
        gridview.adapter = adapter

        gridview.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->

//            if (itemList!![position].fullSizeUrl != null) {
//                val intent = Intent(this, PreviewImageActivity::class.java)
//                intent.putExtra(ConstantsApp.KEY_VALUES_ID, itemList!!.get(position).fullSizeUrl)
//                startActivityForResult(intent, 1000)
//            }
        }
    }

    override fun resumeData() {

    }


}