package com.zamio.adong.ui.project.tab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.project.tab.ui.main.MainWorkOutlineFragment
import com.zamio.adong.ui.project.tab.ui.main.ProductRequirementFragment
import com.zamio.adong.ui.project.tab.ui.main.SectionsPagerAdapter
import kotlinx.android.synthetic.main.item_header_layout.*

class ProjectTabActivity : AppCompatActivity() {
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_tab)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.addFragment(ProductRequirementFragment());
        sectionsPagerAdapter.addFragment(MainWorkOutlineFragment());
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 0)

        imvBack.setOnClickListener {
            onBackPressed()
        }
    }

    public fun getProjectId(): Int {
        return id
    }
}