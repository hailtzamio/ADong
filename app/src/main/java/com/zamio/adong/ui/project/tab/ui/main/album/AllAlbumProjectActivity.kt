package com.zamio.adong.ui.project.tab.ui.main.album

import InformationAdapter
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import com.zamio.adong.model.Information
import com.zamio.adong.model.Trip
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_list_product.*
import kotlinx.android.synthetic.main.item_header_layout.*

class AllAlbumProjectActivity : BaseActivity() {


    var model: Trip? = null
    var id = 1
    var status = 0
    override fun getLayout(): Int {
        return R.layout.activity_list_product
    }

    override fun initView() {
        tvTitle.text = "Album"
        rightButton.setImageResource(R.drawable.icon_update);
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {

            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)

            if (!ConstantsApp.PERMISSION.contains("u")) {
                rightButton.visibility = View.GONE
            }

            mList.add(Information("---", "Chấm công", ""))
            mList.add(Information("---", "Hoàn thành công trình", ""))
            setupRecyclerView(mList)

            rightButton.visibility = View.GONE
        }

    }

    override fun resumeData() {

    }

    val mList = ArrayList<Information>()
    private fun setupRecyclerView(data: List<Information>) {
        val mAdapter = InformationAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter
        mAdapter.onItemClick = {
            when(it) {
                0 -> {
                    val intent = Intent(this, CheckinOutAlbumImage::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_ID, id)
                    startActivityForResult(intent, 1000)
                }

                1 -> {
                    val intent = Intent(this, FinishProjectAlbumActivity::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_ID, id)
                    startActivityForResult(intent, 1000)
                }
            }
        }
    }

}
