package com.zamio.adong.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zamio.adong.R






class TripPagerAdapter(private val context: Context, fm: FragmentManager,private val titles : ArrayList<String>) :
    FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragmentList: ArrayList<Fragment> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position];
    }

    fun addFragment(fragment: Fragment?) {
        mFragmentList.add(fragment!!)
    }

    override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}