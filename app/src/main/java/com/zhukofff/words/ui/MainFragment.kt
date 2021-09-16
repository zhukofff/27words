package com.zhukofff.words.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.zhukofff.words.common.ViewPagerAdapter
import com.zhukofff.words.databinding.FragmentMainBinding
import com.zhukofff.words.fragmentArray

class MainFragment: Fragment() {

    private lateinit var binding : FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = fragmentArray[position]
        }.attach()
        return binding.root
    }
}