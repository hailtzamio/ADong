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
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.project.tab.ProjectTabActivity
import com.zamio.adong.ui.project.tab.ui.main.requirement.ProductRequirementActivity
import kotlinx.android.synthetic.main.activity_detail_project.*
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


        basicInfo.setOnClickListener {
            val intent = Intent(context, BasicInformationActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, (activity as ProjectTabActivity).getProjectId())
            startActivityForResult(intent, 1000)
        }

        rlProduct.setOnClickListener {
            val intent = Intent(context, ProductRequirementActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, (activity as ProjectTabActivity).getProjectId())
            startActivityForResult(intent, 1000)
        }


    }

    override fun onResume() {
        super.onResume()
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
