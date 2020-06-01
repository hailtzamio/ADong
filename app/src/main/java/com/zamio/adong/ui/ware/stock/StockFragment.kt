package com.zamio.adong.ui.ware.stock

import TitleAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.ware.stock.goods_issue.GoodsIssueListActivity
import com.zamio.adong.ui.ware.stock.goods_issue_request.GoodsIssueRequestListActivity
import com.zamio.adong.ui.ware.stock.goods_received.GoodsReceivedNoteListActivity
import com.zamio.adong.ui.ware.stock.stock.StockListActivity
import kotlinx.android.synthetic.main.fragment_main_workeoutline.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class StockFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null
    var currentPage = 0
    var totalPages = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_workeoutline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getData()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()

    }

    private fun setupRecyclerView() {

        val data = ArrayList<String>()
        data.add("Danh sách kho")
        data.add("Line")
        data.add("Danh sách phiếu nhập kho")
        data.add("Danh sách phiếu xuất kho")
        data.add("Line")
        data.add("Danh sách yêu cầu xuất kho")



        val mAdapter = TitleAdapter(data, 2)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            when(product) {
                0 -> goToStockList()
                2 -> GoodsReceivedNoteList()
                3 -> GoodsIssueList()
                5 -> GoodsIssueRequestList()
//                9 -> goToAlbum()
//                10 -> goToCheckinHistory()
            }
        }
    }

    private fun goToStockList() {
        val intent = Intent(context, StockListActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_STATUS, "STOCK")
        startActivityForResult(intent, 1000)
    }

    private fun GoodsReceivedNoteList() {
        val intent = Intent(context, GoodsReceivedNoteListActivity::class.java)
        startActivityForResult(intent, 1000)
    }

    private fun GoodsIssueList() {
        val intent = Intent(context, GoodsIssueListActivity::class.java)
        startActivityForResult(intent, 1000)
    }

    private fun GoodsIssueRequestList() {
        val intent = Intent(context, GoodsIssueRequestListActivity::class.java)
        startActivityForResult(intent, 1000)
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            StockFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
