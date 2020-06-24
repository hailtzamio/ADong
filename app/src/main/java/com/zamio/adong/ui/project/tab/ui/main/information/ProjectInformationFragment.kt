package com.zamio.adong.ui.project.tab.ui.main.information

import RestClient
import TitleAdapter
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.ui.network.Team
import com.elcom.com.quizupapp.ui.network.UserRoles
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.zamio.adong.R
import com.zamio.adong.model.Project
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.project.tab.ProjectTabActivity
import com.zamio.adong.ui.project.tab.ui.main.checkinout.CheckoutInWorkerListActivity
import com.zamio.adong.ui.project.tab.ui.main.file.ProjectFilesActivity
import com.zamio.adong.ui.project.tab.ui.main.requirement.ProductRequirementActivity
import com.zamio.adong.ui.project.tab.ui.main.album.AllAlbumProjectActivity
import com.zamio.adong.ui.worker.MainWorkerActivity
import kotlinx.android.synthetic.main.activity_overview_project.*
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
class ProductInformationFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    var data: Project? = null
    var teamType = Team.ADONG.type
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
        return inflater.inflate(R.layout.activity_overview_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getData()

        teamType = (activity as ProjectTabActivity).getProjectTeam()
        setupRecyclerView()
    }

    private fun goToBaseInformation() {
        val intent = Intent(context, BasicInformationActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID, (activity as ProjectTabActivity).getProjectId())
        startActivityForResult(intent, 1000)
    }

    private fun goToProductRequirement() {
        val intent = Intent(context, ProductRequirementActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID, (activity as ProjectTabActivity).getProjectId())
        startActivityForResult(intent, 1000)
    }

    private fun goToAddingWorkers() {
        val intent = Intent(context, MainWorkerActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID, (activity as ProjectTabActivity).getProjectId())
        startActivityForResult(intent, 1000)
    }

    private fun goToCheckinHistory() {
        val intent = Intent(context, CheckoutInWorkerListActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID, (activity as ProjectTabActivity).getProjectId())
        startActivityForResult(intent, 1000)
    }

    private fun goToAlbum() {
        val intent = Intent(context, AllAlbumProjectActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID, (activity as ProjectTabActivity).getProjectId())
        startActivityForResult(intent, 1000)
    }

    private fun goToFile() {
        val intent = Intent(context, ProjectFilesActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID, (activity as ProjectTabActivity).getProjectId())
        startActivityForResult(intent, 1000)
    }



    private fun goToRegistration() {
        val intent = Intent(context, ProjectRegisterActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID, (activity as ProjectTabActivity).getProjectId())
        startActivityForResult(intent, 1000)
    }

    private fun pauseProject() {
        var title = ""
        title = if(isPauseProject) {
            "Phục hồi công trình?"
        } else {
            "Tạm dừng công trình?"
        }

        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if(isPauseProject) {
                          doResumeProjectApi()
                        } else {
                            doPauseProjectApi()
                        }
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage(title).setPositiveButton("Đồng ý", dialogClickListener)
            .setNegativeButton("Không", dialogClickListener).show()
    }


    private fun doPauseProjectApi() {


        val reason = JsonObject()
        reason.addProperty("note", "Need to Pause")


        showProgessDialog()
        RestClient().getRestService().pauseProject((activity as ProjectTabActivity).getProjectId(), reason).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if( response!!.body() != null && response!!.body().status == 1){
                    showToast("Thành công")
                    getData()
                } else {
                    if(response.errorBody() != null) {
                        val obj = JSONObject(response.errorBody().string())
                        showToast(obj["message"].toString())
                    }
                }
            }
        })
    }


    private fun doResumeProjectApi() {

        if(data == null) {
            return
        }

        val reason = JsonObject()
        reason.addProperty("teamType", data!!.teamType)
        reason.addProperty("teamId", data!!.teamId)
        if(data!!.contractorId != null) {
            reason.addProperty("contractorId", data!!.contractorId)
        } else {
            reason.addProperty("teamId", data!!.teamId)
        }

        reason.addProperty("note", "Need to Pause")

        showProgessDialog()
        RestClient().getRestService().pauseResume((activity as ProjectTabActivity).getProjectId(), reason).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                dismisProgressDialog()
                if( response!!.body() != null && response!!.body().status == 1){
                    showToast("Thành công")
                    getData()
                } else {
                    if(response.errorBody() != null) {
                        val obj = JSONObject(response.errorBody().string())
                        showToast(obj["message"].toString())
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    var isPauseProject = false
    private fun setupRecyclerView() {

        if(recyclerView == null) {
            return
        }

        val data = ArrayList<String>()
        data.add("Thông tin cơ bản")
        data.add("Line")

        if (teamType == Team.ADONG.type) {
            data.add("Thêm công nhân")
        } else {
            if(ConstantsApp.USER_ROLES.contains(UserRoles.Secretary.type)) {
                data.add("Danh sách đăng ký thi công")
            }
        }

        data.add("Danh sách yêu cầu vật tư")
        data.add("Bản thiết kế")
        data.add("Line")
        data.add("Đánh giá công trình")
        data.add("An toàn lao động")

        if (teamType == Team.ADONG.type) {
            data.add("Line")
            data.add("Kho ảnh")
            data.add("Lịch sử điểm danh")
        }

        val mAdapter = TitleAdapter(data,teamType)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            if(ConstantsApp.USER_ROLES.contains(UserRoles.Secretary.type)) {

                when(product) {
                    0 -> goToBaseInformation()
                    2 -> {
                        if(teamType == Team.CONTRACTOR.type) {
                            goToRegistration()
                        } else {
                            goToAddingWorkers()
                        }
                    }
                    3 -> goToProductRequirement()
                    4 -> goToFile()
                    9 -> goToAlbum()
                    10 -> goToCheckinHistory()
                }
            } else {
                when(product) {
                    0 -> goToBaseInformation()
                    2 -> goToProductRequirement()
                    3 -> goToFile()
                    8 -> goToAlbum()
                    9 -> goToCheckinHistory()
                }
            }


        }
    }

    fun getData() {
        RestClient().getInstance().getRestService().getProject((activity as ProjectTabActivity).getProjectId()).enqueue(object :
            Callback<RestData<Project>> {

            override fun onFailure(call: Call<RestData<Project>>?, t: Throwable?) {

            }
            override fun onResponse(
                call: Call<RestData<Project>>?,
                response: Response<RestData<Project>>?
            ) {
                if (response!!.body() != null && response.body().status == 1) {
                    data = response.body().data ?: return
                    if(data != null) {
                        isPauseProject = data!!.status == "PAUSED"
                        setupRecyclerView()
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 101) {

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductInformationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
