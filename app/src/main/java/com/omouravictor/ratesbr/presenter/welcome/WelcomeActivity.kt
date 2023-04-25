package com.omouravictor.ratesbr.presenter.welcome

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.omouravictor.ratesbr.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    private val welcomeViewPager: ViewPager2 by lazy {
        binding.viewPagerWelcome
    }

    private val tabWelcome: TabLayout by lazy {
        binding.tabLayoutWelcome
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initWelcomeViewPager()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.textViewNext.setOnClickListener {
            welcomeViewPager.currentItem++
        }

        binding.textViewPrevious.setOnClickListener {
            welcomeViewPager.currentItem--
        }
    }

    private fun initWelcomeViewPager() {
        welcomeViewPager.adapter = FragmentsViewPagerManager(this)

        welcomeViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.textViewPrevious.visibility = if (position > 0) View.VISIBLE else View.GONE

                binding.textViewNext.visibility = if (position < 2) View.VISIBLE else View.GONE

                super.onPageSelected(position)
            }
        })

        TabLayoutMediator(tabWelcome, welcomeViewPager) { _, _ -> }.attach()
    }
}