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

    private val welcomePager: ViewPager2 by lazy {
        binding.viewPagerWelcome
    }

    private val tabWelcome: TabLayout by lazy {
        binding.tabLayoutWelcome
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initWelcomePager()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.textViewNext.setOnClickListener {
            welcomePager.currentItem++
        }

        binding.textViewPrevious.setOnClickListener {
            welcomePager.currentItem--
        }
    }

    private fun initWelcomePager() {
        welcomePager.adapter = FragmentsViewPagerManager(this)

        welcomePager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.textViewPrevious.visibility = if (position > 0) View.VISIBLE else View.GONE

                binding.textViewNext.visibility = if (position < 2) View.VISIBLE else View.GONE

                super.onPageSelected(position)
            }
        })

        TabLayoutMediator(tabWelcome, welcomePager) { tab, position -> }.attach()
    }
}