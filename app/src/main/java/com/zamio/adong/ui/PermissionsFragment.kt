package com.zamio.adong.ui

import PermissionAdapter
import RestClient
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.ui.network.UserRoles
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.adapter.PermissionGridAdapter
import com.zamio.adong.model.NotificationOb
import com.zamio.adong.model.Permission
import com.zamio.adong.model.Profile
import com.zamio.adong.model.User
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.contractor.MainContractorActivity
import com.zamio.adong.ui.criteria.MainCriteriaActivity
import com.zamio.adong.ui.driver.DriverActionActivity
import com.zamio.adong.ui.driver.MainDriverActivity
import com.zamio.adong.ui.lorry.MainLorryActivity
import com.zamio.adong.ui.notification.NotificationActivity
import com.zamio.adong.ui.product.MainProductActivity
import com.zamio.adong.ui.profile.ProfileActivity
import com.zamio.adong.ui.project.MainProjectActivity
import com.zamio.adong.ui.project.tab.ui.main.registration.ProjectRegistranleActivity
import com.zamio.adong.ui.team.MainTeamActivity
import com.zamio.adong.ui.trip.TripTabActivity
import com.zamio.adong.ui.ware.WareTabActivity
import com.zamio.adong.ui.worker.MainWorkerActivity
import com.zamio.adong.ui.workoutline.MainWorkOutlineActivity
import kotlinx.android.synthetic.main.fragment_notifications.*
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

//        imvBack.visibility = View.GONE
//        rightButton.visibility = View.GONE
//        tvTitle.text = "Trang Chủ"
        getMyRoles()
        getProfile()
        getNotificationNotSeen()
        imvNotification.setOnClickListener {
            val intent = Intent(context, NotificationActivity::class.java)
            startActivity(intent)
        }

        imvAva.setOnClickListener {

            val intent = Intent(context, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getPermission() {
        showProgessDialog()
        RestClient().getRestService().getPermissions().enqueue(object :
            Callback<RestData<ArrayList<Permission>>> {
            override fun onFailure(call: Call<RestData<ArrayList<Permission>>>?, t: Throwable?) {
                Toast.makeText(context, ConstantsApp.TOAST, Toast.LENGTH_SHORT).show()
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<ArrayList<Permission>>>?,
                response: Response<RestData<ArrayList<Permission>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
//                    setupRecyclerView(response.body().data!!)
                    setupGridView(response.body().data!!)
                } else {
                    Toast.makeText(context, ConstantsApp.TOAST, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    var isContractor = false
    private fun getMyRoles() {
        showProgessDialog()
        RestClient().getRestService().getMyRoles().enqueue(object :
            Callback<RestData<ArrayList<User>>> {
            override fun onFailure(call: Call<RestData<ArrayList<User>>>?, t: Throwable?) {
                Toast.makeText(context, ConstantsApp.TOAST, Toast.LENGTH_SHORT).show()
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<ArrayList<User>>>?,
                response: Response<RestData<ArrayList<User>>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {

                    val roles = response.body().data

                    var rolesString = ""

                    if (roles != null) {
                        roles.forEach {

                            rolesString = rolesString + it.name + "-" + it.code + ","

                            if (it.code == "CONTRACTOR") {
                                isContractor = true
                            }
                        }
                    }

                    Log.e("hailpt", " Role ~~~>>> " + rolesString)
                    ConstantsApp.USER_ROLES = rolesString
                    getPermission()

                } else {
                    Toast.makeText(context, ConstantsApp.TOAST, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    private fun setupGridView(data: ArrayList<Permission>) {

        Log.e("hailpt", "count >> " + data.size)
        var permissionString = ""
        data.forEach {
            permissionString = permissionString + it.appEntityCode + "" + it.action + ","
        }

        Log.e("permissionString == ", permissionString)
        ConstantsApp.USER_PERMISSIONS = permissionString

        if (isContractor) {
            data.add(Permission("r", "ContractorProject", 1, 1, 1, "ContractorProject"))
        }

        val permissions = ArrayList<Permission>()

        data.forEach {
            if (it.action == "r" && (it.appEntityCode == "Worker" || it.appEntityCode == "Lorry" || it.appEntityCode == "Warehouse"
                        || it.appEntityCode == "Product"
                        || it.appEntityCode == "Team" || it.appEntityCode == "Driver"
                        || it.appEntityCode == "Contractor" || it.appEntityCode == "CriteriaBundlee" || it.appEntityCode == "Project" || it.appEntityCode == "WorkOutlinee" || it.appEntityCode == "Trip" || it.appEntityCode == "ContractorProject")
            ) {

                if (it.appEntityCode == "Worker") {
                    it.name = "Công Nhân"
                }

                if (it.appEntityCode == "Product") {
                    it.name = "Vật Tư"
                }

                if (it.appEntityCode == "Lorry") {
                    it.name = "Xe"
                }

                if (it.appEntityCode == "Team") {
                    it.name = "Đội Á Đông"
                }

                if (it.appEntityCode == "Driver") {
                    it.name = "Lái Xe"
                }

                if (it.appEntityCode == "Contractor") {
                    it.name = "Nhà Thầu Phụ"
                }

                if (it.appEntityCode == "CriteriaBundle") {
                    it.name = "Bộ Tiêu Chí"
                }

                if (it.appEntityCode == "Project") {
                    it.name = "Công Trình"
                }

                if (it.appEntityCode == "WorkOutline") {
                    it.name = "Hạng Mục"
                }

                if (it.appEntityCode == "Warehouse") {
                    it.name = "Kho Xưởng"
                }

                if (it.appEntityCode == "Trip") {
                    it.name = "Vận chuyển"
                }

                if (it.appEntityCode == "ContractorProject") {
                    it.name = "Đấu Thầu"
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

        if (context == null) {
            return
        }

        adapter = PermissionGridAdapter(context!!, permissions)
        gvFoods.adapter = adapter

        gvFoods.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                val selectedItem = parent.getItemAtPosition(position).toString()
                val product = permissions[position]
                var actionString = ""
                data.forEach {
                    if (it.appEntityCode == product.appEntityCode) {
                        actionString = actionString + "-" + it.action
                    }
                }
                var intent: Intent? = null
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
                    "Warehouse" -> intent = Intent(context, WareTabActivity::class.java)
                    "Trip" -> {
                        if(ConstantsApp.USER_ROLES.contains(UserRoles.Driver.type)) {
                            intent = Intent(context, DriverActionActivity::class.java)
                        } else {
                            intent = Intent(context, TripTabActivity::class.java)
                        }

                    }
                    "ContractorProject" -> intent =
                        Intent(context, ProjectRegistranleActivity::class.java)
                }

                ConstantsApp.PERMISSION = actionString

                if (intent != null) {
                    startActivity(intent)
                    activity!!.overridePendingTransition(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                    )
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

    private fun setupRecyclerView(data: List<Permission>) {

        val permissions = ArrayList<Permission>()

        data.forEach {
            if (it.action == "r" && (it.appEntityCode == "Worker" || it.appEntityCode == "Lorry" || it.appEntityCode == "Product")) {

                if (it.appEntityCode == "Worker") {
                    it.name = "Công Nhân"
                }

                if (it.appEntityCode == "Product") {
                    it.name = "Vật Tư"
                }

                if (it.appEntityCode == "Lorry") {
                    it.name = "Xe"
                }

                if (it.appEntityCode == "Team") {
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
                if (it.appEntityCode == product.appEntityCode) {
                    actionString = actionString + "-" + it.action
                }
            }

            var intent: Intent? = null
            when (product.name) {
                "Vật Tư" -> intent = Intent(context, MainProductActivity::class.java)
                "Xe" -> intent = Intent(context, MainLorryActivity::class.java)
                "Công Nhân" -> intent = Intent(context, MainWorkerActivity::class.java)
            }

            ConstantsApp.PERMISSION = actionString

            if (intent != null) {
                intent.putExtra(ConstantsApp.KEY_PERMISSION, actionString)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    private fun getProfile() {

        showProgessDialog()
        RestClient().getInstance().getRestService().getProfile().enqueue(object :
            Callback<RestData<Profile>> {
            override fun onFailure(call: Call<RestData<Profile>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<RestData<Profile>>?,
                response: Response<RestData<Profile>>?
            ) {
                dismisProgressDialog()
                if (response?.body() != null && response.body()!!.data != null) {

                    val profile = response.body()!!.data

                    if (profile != null) {
                        if (profile.avatarUrl != null && profile.avatarUrl != "") {
                            Picasso.get().load(profile.avatarUrl).error(R.drawable.ava).into(imvAva)
                        }
                    }

                }
            }
        })
    }

    private fun getNotificationNotSeen() {

        RestClient().getInstance().getRestService()
            .getNotificationCount()
            .enqueue(object :
                Callback<RestData<NotificationOb>> {
                override fun onFailure(
                    call: Call<RestData<NotificationOb>>?,
                    t: Throwable?
                ) {

                }

                override fun onResponse(
                    call: Call<RestData<NotificationOb>>?,
                    response: Response<RestData<NotificationOb>>?
                ) {

                    if (response!!.body() != null && response.body().status == 1) {

                        if(response.body().data == null) {
                            return
                        }
                        if(tvNotification != null) {
                            tvNotification.text = (response.body().data!!.notSeenCount ?: 0).toString()
                            if(response.body().data!!.notSeenCount ?: 0 == 0) {
                                rlNotification.visibility = View.GONE
                            } else {
                                rlNotification.visibility = View.VISIBLE
                            }
                        }

                    }
                }
            })
    }
}
