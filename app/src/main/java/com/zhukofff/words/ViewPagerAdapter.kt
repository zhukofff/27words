package com.zhukofff.words

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.Fragment

class ViewPagerAdapter(manager : FragmentManager) :
    FragmentPagerAdapter(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList : ArrayList<Fragment> ?= null
    private val fragmentListTitles : MutableList<String> ?= null


    override fun getItem(position : Int) : Fragment {
        return fragmentList!!.get(position)
    }

    override fun getCount() : Int {
        return fragmentList!!.size
    }

    override fun getPageTitle(position: Int) : CharSequence {
        return fragmentListTitles!!.get(position)
    }

    fun addFragment(fragment : Fragment, name : String){
        fragmentList!!.add(fragment)
        fragmentListTitles!!.add(name)
    }

}