package com.zhukofff.words

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.zhukofff.words.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter : ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(TranslateFragment(), "Translate")
        adapter.addFragment(StudyFragment(), "Study")

        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.viewPager.addOnPageChangeListener(object: OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageScrollStateChanged(state: Int) {
                TODO("Not yet implemented")
            }

            override fun onPageSelected(position: Int) {
                TODO("Not yet implemented")
            }

        })
        // TODO: after creating translate, study, about fragments need to add
        // them using addFragment() method
        //adapter.addFragment()
    }
}