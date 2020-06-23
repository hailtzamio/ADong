package com.zamio.adong.ui.trip

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.elcom.com.quizupapp.ui.network.UserRoles
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.zamio.adong.R
import com.zamio.adong.adapter.TripPagerAdapter
import com.zamio.adong.model.Transport
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.popup.HoldSimDialog
import kotlinx.android.synthetic.main.activity_trip_tab.*

private val TAB_TITLES_2 = arrayOf(
    R.string.transport_1,
    R.string.transport_2
)

class TripTabActivity : AppCompatActivity() {
    var id = 0
    var position = 0
    val transportRequestFragment = TransportRequestFragment()
    val transportRequestDoneFragment = TransportRequestDoneFragment()
    val transportRequestProcessingFragment = TransportRequestProcessingFragment()
    var titles = ArrayList<String>()
    val tripFragment = TripFragment()
    var transports = ArrayList<Transport>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_tab)


        if(ConstantsApp.USER_ROLES.contains(UserRoles.CarManagement.type)) {
            titles.add("YCVC")
        }

        if(ConstantsApp.USER_ROLES.contains(UserRoles.Driver.type)) {
            titles.add("Chuyến đi")
        }

        val sectionsPagerAdapter = TripPagerAdapter(this, supportFragmentManager, titles)

        if(ConstantsApp.USER_ROLES.contains(UserRoles.CarManagement.type)) {
            sectionsPagerAdapter.addFragment(transportRequestFragment)
        }

        if(ConstantsApp.USER_ROLES.contains(UserRoles.Driver.type)) {
            sectionsPagerAdapter.addFragment(tripFragment)
        }

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

        rightButton.setOnClickListener {

            val dialog = HoldSimDialog(this)
            dialog.show()

            dialog.onItemClick = {
                ConstantsApp.transportsChoose = transports
                if(it == 1) {
                    val intent = Intent(this, CreateTripActivity::class.java)
                    startActivity(intent)
                    this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    val intent = Intent(this, TripActivity::class.java)
                    intent.putExtra(ConstantsApp.KEY_VALUES_ID,1)
                    startActivity(intent)
                    this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }

            }
        }

    }

    fun getProjectId(): Int {
        return id
    }

    fun setTitle() {

    }

    fun setTrips(transports : ArrayList<Transport>) {
        this.transports = transports

        var isShow = false
        transports.forEach {
            if(it.isSelected == true) {
                isShow = true
            }
        }

        if(isShow) {
            rightButton.visibility = View.VISIBLE
        } else {
            rightButton.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 100) {
            setTrips(ArrayList<Transport>())
        }
    }

}