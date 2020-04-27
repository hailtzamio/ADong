package com.zamio.adong.ui

import PermissionAdapter
import RestClient
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.R
import com.zamio.adong.adapter.PermissionGridAdapter
import com.zamio.adong.model.Permission
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.contractor.MainContractorActivity
import com.zamio.adong.ui.criteria.MainCriteriaActivity
import com.zamio.adong.ui.driver.MainDriverActivity
import com.zamio.adong.ui.lorry.MainLorryActivity
import com.zamio.adong.ui.product.MainProductActivity
import com.zamio.adong.ui.project.MainProjectActivity
import com.zamio.adong.ui.team.MainTeamActivity
import com.zamio.adong.ui.worker.MainWorkerActivity
import com.zamio.adong.ui.workoutline.MainWorkOutlineActivity
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.item_header_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PermissionsFragment : BaseFragment() {

    var adapter: PermissionGridAdapter? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imvBack.visibility = View.GONE
        rightButton.visibility = View.GONE
        tvTitle.text = "Trang Chủ"
        getPermission()
    }

    private fun getPermission(){
        showProgessDialog()
        RestClient().getRestService().getPermissions().enqueue(object :
            Callback<RestData<ArrayList<Permission>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Permission>>>?, t: Throwable?) {
                Toast.makeText(context, ConstantsApp.TOAST, Toast.LENGTH_SHORT).show()
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<Permission>>>?, response: Response<RestData<ArrayList<Permission>>>?) {
                dismisProgressDialog()
                if( response!!.body() != null && response!!.body().status == 1){
//                    setupRecyclerView(response.body().data!!)
                    setupGridView(response!!.body().data!!)
                } else {
                    Toast.makeText(context, ConstantsApp.TOAST, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupGridView(data:ArrayList<Permission>){

        val permissions = ArrayList<Permission>()

        data.forEach {
            if (it.action == "r" &&  ( it.appEntityCode == "Worker" || it.appEntityCode == "Lorry"
                        || it.appEntityCode == "Product"
                        || it.appEntityCode == "Team" || it.appEntityCode == "Driver"
                        || it.appEntityCode == "Contractor" || it.appEntityCode  == "CriteriaBundle" || it.appEntityCode  == "Project"|| it.appEntityCode  == "WorkOutline" )) {

                if (it.appEntityCode == "Worker" ) {
                    it.name = "Công Nhân"
                }

                if (it.appEntityCode == "Product" ) {
                    it.name = "Vật Tư"
                }

                if (it.appEntityCode == "Lorry" ) {
                    it.name = "Xe"
                }

                if (it.appEntityCode == "Team" ) {
                    it.name = "Đội Thi Công"
                }

                if (it.appEntityCode == "Driver" ) {
                    it.name = "Lái Xe"
                }

                if (it.appEntityCode == "Contractor" ) {
                    it.name = "Nhà Thầu Phụ"
                }

                if (it.appEntityCode == "CriteriaBundle" ) {
                    it.name = "Bộ Tiêu Chí"
                }

                if (it.appEntityCode == "Project" ) {
                    it.name = "Công Trình"
                }

                if (it.appEntityCode == "WorkOutline" ) {
                    it.name = "Hạng Mục"
                }

                permissions.add(it)

            }
        }

        var count = permissions.size
        for (i in 0 until count) {
            var j = i + 1
            while (j < count) {
                if (permissions[i].appEntityCode == permissions[j].appEntityCode) {
                    permissions.removeAt(j--)
                    count--
                }
                j++
            }
        }

        adapter = PermissionGridAdapter(context!!, permissions)
        gvFoods.adapter = adapter

        gvFoods.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                val selectedItem = parent.getItemAtPosition(position).toString()
                val product = permissions[position]
                var actionString = ""
                data.forEach {
                    if (it.appEntityCode == product.appEntityCode){
                        actionString = actionString + "-" + it.action
                    }
                }
                var intent:Intent? = null
                when (product.appEntityCode) {
                    "Product" -> intent = Intent(context, MainProductActivity::class.java)
                    "Lorry" -> intent = Intent(context, MainLorryActivity::class.java)
                    "Worker" -> intent = Intent(context, MainWorkerActivity::class.java)
                    "Team" -> intent = Intent(context, MainTeamActivity::class.java)
                    "Driver" -> intent = Intent(context, MainDriverActivity::class.java)
                    "Contractor" -> intent = Intent(context, MainContractorActivity::class.java)
                    "CriteriaBundle" -> intent = Intent(context, MainCriteriaActivity::class.java)
                    "Project" -> intent = Intent(context, MainProjectActivity::class.java)
                    "WorkOutline" -> intent = Intent(context, MainWorkOutlineActivity::class.java)
                }

                ConstantsApp.PERMISSION = actionString

                if(intent != null){
                    startActivity(intent)
                    activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }

            }
    }

    private fun removeDuplicates(list: ArrayList<Permission>) {
        var count = list.size
        for (i in 0 until count) {
            var j = i + 1
            while (j < count) {
                if (list[i].appEntityCode == list[j].appEntityCode) {
                    list.removeAt(j--)
                    count--
                }
                j++
            }
        }
    }

    private fun setupRecyclerView(data:List<Permission>){

        val permissions = ArrayList<Permission>()

        data.forEach {
            if (it.action == "r" &&  ( it.appEntityCode == "Worker" || it.appEntityCode == "Lorry" || it.appEntityCode == "Product")){

                if (it.appEntityCode == "Worker" ){
                    it.name = "Công Nhân"
                }

                if (it.appEntityCode == "Product" ){
                    it.name = "Vật Tư"
                }

                if (it.appEntityCode == "Lorry" ){
                    it.name = "Xe"
                }

                if (it.appEntityCode == "Team" ){
                    it.name = "Đội Thi Công"
                }

                permissions.add(it)
            }
        }

        val mAdapter = PermissionAdapter(permissions)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            var actionString = ""
            data.forEach {
                if (it.appEntityCode == product.appEntityCode){
                    actionString = actionString + "-" + it.action
                }
            }

            var intent:Intent? = null
            when (product.name) {
                "Vật Tư" -> intent = Intent(context, MainProductActivity::class.java)
                "Xe" -> intent = Intent(context, MainLorryActivity::class.java)
                "Công Nhân" -> intent = Intent(context, MainWorkerActivity::class.java)
            }

            ConstantsApp.PERMISSION = actionString

            if(intent != null){
                intent.putExtra(ConstantsApp.KEY_PERMISSION, actionString)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }
}
