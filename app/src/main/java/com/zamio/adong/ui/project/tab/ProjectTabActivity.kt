package com.zamio.adong.ui.project.tab

import RestClient
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.zamio.adong.R
import com.zamio.adong.model.Project
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.project.tab.ui.main.SectionsPagerAdapter
import com.zamio.adong.ui.project.tab.ui.main.checkinout.CheckInOutActivity
import com.zamio.adong.ui.project.tab.ui.main.checkinout.CheckinOutAlbumImage
import com.zamio.adong.ui.project.tab.ui.main.checkinout.CheckoutInWorkerListActivity
import com.zamio.adong.ui.project.tab.ui.main.information.ProductInformationFragment
import com.zamio.adong.ui.project.tab.ui.main.information.UpdateProjectActivity
import com.zamio.adong.ui.project.tab.ui.main.requirement.CreateProductRequirementActivity
import com.zamio.adong.ui.project.tab.ui.main.requirement.ProductRequirementActivity
import com.zamio.adong.ui.project.tab.ui.main.workers.ProjectWorkersFragment
import com.zamio.adong.ui.project.tab.ui.main.workoutline.MainWorkOutlineFragment
import com.zamio.adong.ui.worker.MainWorkerActivity
import kotlinx.android.synthetic.main.activity_project_tab.*
import kotlinx.android.synthetic.main.item_header_layout.imvBack
import kotlinx.android.synthetic.main.material_design_floating_action_menu.*
import kotlinx.android.synthetic.main.material_design_floating_worker_action_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProjectTabActivity : AppCompatActivity() {
    var id = 0
    var position = 0
    var statusProject = ""
//    val producPage = ProductRequirementFragment()
    val workerPage = ProjectWorkersFragment()
    val informationPage = ProductInformationFragment()
    var data: Project? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_tab)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.addFragment(workerPage)
//        sectionsPagerAdapter.addFragment(producPage)
        sectionsPagerAdapter.addFragment(MainWorkOutlineFragment())
        sectionsPagerAdapter.addFragment(informationPage)


        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: ImageView = findViewById(R.id.fab)

        imvCheckInOut.visibility = View.GONE
        imvCheckInOut.setOnClickListener {
            val intent = Intent(this, CheckInOutActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, id)
            startActivityForResult(intent, 1000)
        }


        tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                position = tab.position

                when (position) {
                    2 -> fab.visibility = View.GONE
                    1 -> fab.visibility = View.GONE
                    0 -> fab.visibility = View.VISIBLE
                }
            }
        })

        fab.setOnClickListener { view ->
            goToUploadImage()
        }

        id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 0)
        data = intent.extras!!.get(ConstantsApp.KEY_VALUES_OBJECT) as Project

//        getData()

        statusProject = intent.getStringExtra(ConstantsApp.KEY_VALUES_STATUS)
        tvTitle.text = intent.getStringExtra(ConstantsApp.KEY_VALUES_TITLE)

        imvBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun goToCreateProductRequirement() {
        val intent = Intent(this, CreateProductRequirementActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID, id)
        startActivityForResult(intent, 1000)
    }



    private fun goToUploadImage() {
        workerPage.pickImageFromAlbum()
    }

    fun getData() {
        RestClient().getInstance().getRestService().getProject(id).enqueue(object :
            Callback<RestData<Project>> {

            override fun onFailure(call: Call<RestData<Project>>?, t: Throwable?) {

            }
            override fun onResponse(
                call: Call<RestData<Project>>?,
                response: Response<RestData<Project>>?
            ) {
                if (response!!.body() != null && response.body().status == 1) {
                    data = response.body().data ?: return
                }
            }
        })
    }

    fun getProjectId(): Int {
        return id
    }

    fun getStatus(): String {
        return statusProject
    }
    fun getProject() : Project{
        return data!!
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 101) {
//            producPage.getData(0)
        }

        if (resultCode == 102) {
            workerPage.resetData()
            workerPage.getData(0)
        }
    }

}