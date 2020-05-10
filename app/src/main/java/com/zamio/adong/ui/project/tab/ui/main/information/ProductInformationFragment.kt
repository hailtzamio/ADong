package com.zamio.adong.ui.project.tab.ui.main.information

import RestClient
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.Project
import com.zamio.adong.ui.project.tab.ProjectTabActivity
import kotlinx.android.synthetic.main.activity_detail_project.*
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
        return inflater.inflate(R.layout.activity_detail_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProject((activity as ProjectTabActivity).getProjectId())
        header.visibility = View.GONE
        tvOk.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
    }

    fun removeProjectPopup() {
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if ((activity as ProjectTabActivity).getProjectId() != 0) {
                            removeProject()
                        }
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage("Xóa công trình?").setPositiveButton("Đồng ý", dialogClickListener)
            .setNegativeButton("Không", dialogClickListener).show()
    }

    var data: Project? = null
    private fun getProject(id: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getProject(id).enqueue(object :
            Callback<RestData<Project>> {

            override fun onFailure(call: Call<RestData<Project>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<Project>>?,
                response: Response<RestData<Project>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response!!.body().status == 1) {
                    data = response.body().data ?: return
                    tvName.text = data!!.name
                    tvAddress.text = data!!.address
                    tvChooseDate.text = data!!.plannedStartDate
                    tvChooseEndDate.text = data!!.plannedEndDate
                    tvManagerName.text = data!!.managerFullName
                    tvDeputyManagerName.text = data!!.deputyManagerFullName
                    tvLeaderName.text = data!!.supervisorFullName
                    tvSecretaryName.text = data!!.secretaryFullName
                    tvChooseTeamOrContractor.text = data!!.deputyManagerFullName
                    if (data!!.teamType == "ADONG") {
                        tvContractorOrTeam.text = "Đội Á đông"
                    } else {
                        tvContractorOrTeam.text = data!!.contractorName
                        tvContractorOrTeamLabel.text = "Nhà thầu phụ"
                    }
                }
            }
        })
    }

    private fun removeProject() {
        showProgessDialog()
        RestClient().getInstance().getRestService().removeProject((activity as ProjectTabActivity).getProjectId()).enqueue(object :
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
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show()
                    activity!!.setResult(100)
                    activity!!.finish()
                } else {
                    val obj = JSONObject(response.errorBody().string())
                    Toast.makeText(context, obj["message"].toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 101){

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
            ProductInformationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
