package com.ubc.words.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ubc.words.ui.study.StudyFragment
import com.ubc.words.ui.translate.TranslateFragment

private const val NUM_TABS = 2

class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle){

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return TranslateFragment()
        }
        return StudyFragment()
    }


}
