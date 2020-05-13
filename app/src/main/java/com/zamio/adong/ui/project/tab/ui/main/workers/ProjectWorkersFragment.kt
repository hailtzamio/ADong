package com.zamio.adong.ui.project.tab.ui.main.workers

import RestClient
import WorkerAdapter
import WorkerCheckinOutAdapter
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.CheckinOut
import com.zamio.adong.model.Worker
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.project.tab.ProjectTabActivity
import com.zamio.adong.ui.worker.DetailWorkerActivity
import com.zamio.adong.utils.PaginationScrollListener
import kotlinx.android.synthetic.main.fragment_main_worker.*
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
class ProjectWorkersFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var data = ArrayList<Worker>()
    var projectId = 0
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

        return inflater.inflate(R.layout.fragment_main_workeoutline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetData()
        setupRecyclerView()
        getData(0)
        projectId = (activity as ProjectTabActivity).getProjectId()
    }

    override fun onResume() {
        super.onResume()
    }

    fun resetData() {
        data.clear()
    }

    fun getData(pPage:Int){
        data.clear()
        showProgessDialog()
        RestClient().getInstance().getRestService().getProjectWorkers((activity as ProjectTabActivity).getProjectId(),pPage).enqueue(object :
            Callback<RestData<List<Worker>>> {
            override fun onFailure(call: Call<RestData<List<Worker>>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<List<Worker>>>?, response: Response<RestData<List<Worker>>>?) {
                dismisProgressDialog()
                if(response!!.body() != null && response.body().status == 1){
                    data.addAll(response.body().data!!)
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    val mAdapter = WorkerCheckinOutAdapter(data)
    private fun setupRecyclerView(){
        val layoutManager = LinearLayoutManager(context)
        if( recyclerView != null) {
            recyclerView.layoutManager = layoutManager
            recyclerView.setHasFixedSize(false)
            recyclerView.adapter = mAdapter

            mAdapter.onItemClick = { product ->
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                val ids = ArrayList<Int>()
                                ids.add(product.id)

                                val check = CheckinOut(projectId, ids)
                                if (product.workingStatus == "idle") {
                                    checkin(check)
                                } else {
                                    checkout(check)
                                }
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setMessage("Điểm danh?")
                    .setPositiveButton("Đồng ý", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show()
            }
        }
    }
    private fun checkout(checkinOut: CheckinOut) {
        showProgessDialog()
        RestClient().getInstance().getRestService().checkout(checkinOut).enqueue(object :
            Callback<RestData<JsonElement>> {
            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    Toast.makeText(context, "Chấm giờ ra thành công", Toast.LENGTH_SHORT).show()
                    getData(0)
                }
            }
        })
    }

    private fun checkin(checkinOut: CheckinOut) {
        showProgessDialog()
        RestClient().getInstance().getRestService().checkin(checkinOut).enqueue(object :
            Callback<RestData<JsonElement>> {
            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    Toast.makeText(context, "Chấm giờ vào thành công", Toast.LENGTH_SHORT).show()
                    getData(0)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data1: Intent?) {
        super.onActivityResult(requestCode, resultCode, data1)
        if(resultCode == 101){
            data.clear()
            getData(0)
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
            ProjectWorkersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
