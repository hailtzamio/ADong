package com.zamio.adong.ui.project.tab

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.project.tab.ui.main.SectionsPagerAdapter
import com.zamio.adong.ui.project.tab.ui.main.checkinout.CheckInOutActivity
import com.zamio.adong.ui.project.tab.ui.main.information.ProductInformationFragment
import com.zamio.adong.ui.project.tab.ui.main.information.UpdateProjectActivity
import com.zamio.adong.ui.project.tab.ui.main.requirement.CreateProductRequirementActivity
import com.zamio.adong.ui.project.tab.ui.main.requirement.ProductRequirementFragment
import com.zamio.adong.ui.project.tab.ui.main.workers.ProjectWorkersFragment
import com.zamio.adong.ui.project.tab.ui.main.workoutline.MainWorkOutlineFragment
import com.zamio.adong.ui.worker.MainWorkerActivity
import kotlinx.android.synthetic.main.activity_project_tab.*
import kotlinx.android.synthetic.main.item_header_layout.imvBack
import kotlinx.android.synthetic.main.material_design_floating_action_menu.*


class ProjectTabActivity : AppCompatActivity() {
    var id = 0
    var position = 0
    val producPage = ProductRequirementFragment()
    val workerPage = ProjectWorkersFragment()
    val informationPage = ProductInformationFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_tab)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.addFragment(informationPage);
        sectionsPagerAdapter.addFragment(producPage);
        sectionsPagerAdapter.addFragment(MainWorkOutlineFragment());
        sectionsPagerAdapter.addFragment(workerPage);

        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        hideFabShowFloat()
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
                    0 -> hideFabShowFloat()
                    1 -> showFabHideFloat()
                    2 -> hideFabHideFloat()
                    3 -> showFabHideFloat()
                }
            }
        })

        fab.setOnClickListener { view ->
            when (position) {
                1 -> goToCreateProductRequirement()
                2 -> setupItemWorkOutLine()
                3 -> goToAddingWorkers()
            }
        }

        id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 0)

        imvBack.setOnClickListener {
            onBackPressed()
        }

        floatingActionButton1.setOnClickListener {
            informationPage.removeProjectPopup()
        }

        floatingActionButton2.setOnClickListener {
            val intent = Intent(this, UpdateProjectActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, id)
            startActivityForResult(intent, 1000)
        }

        floatingActionButton3.setOnClickListener {

        }
    }

    private fun goToCreateProductRequirement() {
        val intent = Intent(this, CreateProductRequirementActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID, id)
        startActivityForResult(intent, 1000)
    }

    private fun setupItemWorkOutLine() {
        fab.visibility = View.GONE
    }

    private fun hideFabShowFloat() {
        fab.visibility = View.GONE
        viewFloat.visibility = View.VISIBLE
    }

    private fun showFabHideFloat() {
        fab.visibility = View.VISIBLE
        viewFloat.visibility = View.GONE
    }

    private fun hideFabHideFloat() {
        fab.visibility = View.GONE
        viewFloat.visibility = View.GONE
    }

    private fun goToAddingWorkers() {
        val intent = Intent(this, MainWorkerActivity::class.java)
        intent.putExtra(ConstantsApp.KEY_VALUES_ID, id)
        startActivityForResult(intent, 1000)
    }

    public fun getProjectId(): Int {
        return id
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 99) {
            informationPage.getData(id)
        }

        if (resultCode == 101) {
            producPage.getData(0)
        }

        if (resultCode == 102) {
            workerPage.getData(0)
        }
    }

}