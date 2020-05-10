package com.zamio.adong.ui.worker

import RestClient
import WorkerAdapter
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.R
import com.zamio.adong.adapter.PaginationScrollListener
import com.zamio.adong.model.Worker
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.fragment_main_worker.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainWorkerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainWorkerFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    var currentPage = 0
    var totalPages = 0
    var products:List<Worker>? = null
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
        return inflater.inflate(R.layout.fragment_main_worker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTitle.text = "Công Nhân"
        rightButton.setOnClickListener {
            val intent = Intent(context, CreateWorkerActivity::class.java)
            intent.putExtra("EMAIL", "")
            startActivityForResult(intent,1000)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        imvBack.setOnClickListener {
            activity!!.setResult(102)
            activity!!.finish()
        }

        if(!ConstantsApp.PERMISSION!!.contains("c")){
            rightButton.visibility = View.GONE
        }

        edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if((activity as MainWorkerActivity).getProjectId() != 0) {
                    getWorkersNotLeaders(0)
                } else {
                    getProducts(0)
                }
                return@OnEditorActionListener true
            }
            false
        })

        imvSearch.setOnClickListener {
            if((activity as MainWorkerActivity).getProjectId() != 0) {
                getWorkersNotLeaders(0)
            } else {
                getProducts(0)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if((activity as MainWorkerActivity).getProjectId() != 0) {
            rightButton.visibility = View.GONE
            getWorkersNotLeaders(0)
        } else {
            getProducts(0)
        }
    }

    private fun getProducts(page:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getWorkers(page,edtSearch.text.toString()).enqueue(object :
            Callback<RestData<List<Worker>>> {
            override fun onFailure(call: Call<RestData<List<Worker>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<List<Worker>>>?, response: Response<RestData<List<Worker>>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response!!.body().status == 1){
                    products = response.body().data!!
                    setupRecyclerView()
                    totalPages = response.body().pagination!!.totalPages!!
                }
            }
        })
    }

    private fun getWorkersNotLeaders(page:Int){
        showProgessDialog()
        RestClient().getInstance().getRestService().getWorkersNotLeaders(page,edtSearch.text.toString()).enqueue(object :
            Callback<RestData<List<Worker>>> {
            override fun onFailure(call: Call<RestData<List<Worker>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<List<Worker>>>?, response: Response<RestData<List<Worker>>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response!!.body().status == 1){
                    products = response.body().data!!
                    setupRecyclerView()
                    totalPages = response.body().pagination!!.totalPages!!
                }
            }
        })
    }

    private fun addWorkerToProject(projectId:Int, workerId:Int) {
        val workerJs = JsonObject()
        workerJs.addProperty("workerId",workerId)
        showProgessDialog()
        RestClient().getInstance().getRestService().addWorkerToProject(projectId,workerJs).enqueue(object :
            Callback<RestData<JsonElement>> {
            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response.body().status == 1){
                    Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show()
//                    activity!!.setResult(102)
////                    activity!!.finish()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    Toast.makeText(context, obj["message"].toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupRecyclerView(){

        val mAdapter = WorkerAdapter(products!!)
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            if((activity as MainWorkerActivity).getProjectId() != 0) {

                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                addWorkerToProject((activity as MainWorkerActivity).getProjectId(), product.id)
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setMessage("Thêm công nhân?")
                    .setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show()

            } else {
                val intent = Intent(context, DetailWorkerActivity::class.java)
                intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.id)
                startActivityForResult(intent,1000)
                activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

        var isLastPage: Boolean = false
        var isLoading: Boolean = false

        recyclerView?.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager ) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
//                if((currentPage + 1) < totalPages){
//                    getProducts(currentPage++)
//                }
//                currentPage += 1
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 100){
//            getProducts(0)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainWorkerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainWorkerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
