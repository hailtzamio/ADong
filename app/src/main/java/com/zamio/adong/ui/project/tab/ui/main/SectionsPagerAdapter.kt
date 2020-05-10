package com.zamio.adong.ui.project.tab.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zamio.adong.R


private val TAB_TITLES = arrayOf(
    R.string.tab_text_0,
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragmentList: ArrayList<Fragment> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position];
    }

    fun addFragment(fragment: Fragment?) {
        mFragmentList.add(fragment!!)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}