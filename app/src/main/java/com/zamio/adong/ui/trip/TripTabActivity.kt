package com.zamio.adong.ui.trip

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.zamio.adong.R
import com.zamio.adong.adapter.WareHousePagerAdapter
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.item_header_layout.*



class TripTabActivity : AppCompatActivity() {
    var id = 0
    var position = 0
    val stockFrag = TransportRequestFragment()
    val factoryFrag = TripFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_tab)
        val sectionsPagerAdapter = WareHousePagerAdapter(this, supportFragmentManager, 2)
        sectionsPagerAdapter.addFragment(stockFrag)
        sectionsPagerAdapter.addFragment(factoryFrag)
        tvTitle.text = "Vận Chuyển"
        rightButton.visibility = View.GONE

        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                position = tab.position

                when (position) {

                    

                }
            }
        })

        imvBack.setOnClickListener {
            onBackPressed()
        }

    }

    fun getProjectId(): Int {
        return id
    }
    fun setTitle() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 101) {
//            producPage.getData(0)
        }
    }

}