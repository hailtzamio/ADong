package com.zamio.adong.ui.ware.stock.stock

import TitleAdapter
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.Team
import com.zamio.adong.R
import com.zamio.adong.model.WareHouse
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.ware.stock.factory.ManufactureRequestListActivity
import com.zamio.adong.ui.ware.stock.goods_issue.GoodsIssueListActivity
import com.zamio.adong.ui.ware.stock.goods_issue_request.GoodsIssueRequestListActivity
import kotlinx.android.synthetic.main.activity_stock_list.*
import kotlinx.android.synthetic.main.item_header_layout.*

class FactoryListTitleActivity : BaseActivity() {

    var mList = ArrayList<WareHouse>()
    var isChooseStock = false // CreateGoodsReceivedNoteActivity
    var type = "STOCK"
    var id = 0
    override fun getLayout(): Int {
        return R.layout.activity_stock_list
    }

    override fun initView() {
        rightButton.visibility = View.GONE
        rightButton.setOnClickListener {
            val intent = Intent(this, CreateStockActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_STATUS, type)
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun initData() {
        if(intent.hasExtra(ConstantsApp.KEY_VALUES_STATUS)) {
            type = intent.getStringExtra(ConstantsApp.KEY_VALUES_STATUS)!!
            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 0)
            if(type == "FACTORY") {
                tvTitle.text = "Danh Sách Xưởng"
            } else {
                tvTitle.text = "Danh Sách Kho"
            }
        }
    }

    override fun resumeData() {
        mList.clear()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {

        val data = ArrayList<String>()
        data.add("Danh sách yêu cầu sản xuất")
        data.add("Danh sách phiếu xuất xưởng")

        val mAdapter = TitleAdapter(data, Team.STOCK.type)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            when(product) {
                0-> GoodsReceivedNoteList()
                1 -> GoodsIssueList()
            }
        }
    }

    private fun GoodsReceivedNoteList() {
        val intent = Intent(this, ManufactureRequestListActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID, id)
        startActivityForResult(intent, 1000)
    }

    private fun GoodsIssueList() {
        val intent = Intent(this, GoodsIssueListActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID, id)
        startActivityForResult(intent, 1000)
    }

    private fun GoodsIssueRequestList() {
        val intent = Intent(this, GoodsIssueRequestListActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID, id)
        startActivityForResult(intent, 1000)
    }
}
