package com.zamio.adong.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zamio.adong.R


private val TAB_TITLES = arrayOf(
    R.string.ware_0,
    R.string.ware_1
)

private val TAB_TITLES_2 = arrayOf(
    R.string.transport_1,
    R.string.transport_2,
    R.string.transport_3,
    R.string.transport_4
)

class WareHousePagerAdapter(private val context: Context, fm: FragmentManager,private val type : Int) :
    FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragmentList: ArrayList<Fragment> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position];
    }

    fun addFragment(fragment: Fragment?) {
        mFragmentList.add(fragment!!)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if(type == 1) {
            return context.resources.getString(TAB_TITLES[position])
        } else {
            return context.resources.getString(TAB_TITLES_2[position])
        }

    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}