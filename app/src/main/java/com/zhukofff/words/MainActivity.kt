package com.zhukofff.words

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhukofff.words.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter : ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = adapter

        // TODO: after creating translate, study, about fragments need to add
        // them using addFragment() method
        //adapter.addFragment()
    }
}