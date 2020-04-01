package com.zamio.adong.ui

import PermissionAdapter
import RestClient
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.zamio.adong.MainActivity
import com.zamio.adong.R
import com.zamio.adong.adapter.PermissionGridAdapter
import com.zamio.adong.model.Permission
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.lorry.MainLorryActivity
import com.zamio.adong.ui.product.MainProductActivity
import com.zamio.adong.ui.team.MainTeamActivity
import com.zamio.adong.ui.worker.MainWorkerActivity
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
                dismisProgressDialog()
            }

            override fun onResponse(call: Call<RestData<ArrayList<Permission>>>?, response: Response<RestData<ArrayList<Permission>>>?) {
                dismisProgressDialog()
                if( response!!.body() != null && response!!.body().status == 1){
//                    setupRecyclerView(response.body().data!!)
                    setupGridView(response!!.body().data!!)
                }
            }
        })
    }

    private fun triggerRebirth(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.putExtra("KEY_RESTART_INTENT", nextIntent)
        context.startActivity(intent)
//        if (context is Activity) {
//            context.finish()
//        }
//        Runtime.getRuntime().exit(0)
    }

    private fun setupGridView(data:ArrayList<Permission>){

        val permissions = ArrayList<Permission>()

        data.forEach {
            if (it.action == "r" &&  ( it.appEntityCode == "Worker" || it.appEntityCode == "Lorry" || it.appEntityCode == "Product"|| it.appEntityCode == "Team" )){

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
                }

                ConstantsApp.PERMISSION = actionString

                if(intent != null){
                    startActivity(intent)
                    activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }

            }
    }

    private fun setupRecyclerView(data:List<Permission>){

        val permissions = ArrayList<Permission>()

        data.forEach {
            if (it.action == "r" &&  ( it.appEntityCode == "Worker" || it.appEntityCode == "Lorry" || it.appEntityCode == "Product" )){

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
