package com.omouravictor.ratesbr.presenter.welcome

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.omouravictor.ratesbr.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initWelcomeViewPager()
        setClickListeners()
    }

    private fun initWelcomeViewPager() {
        binding.viewPagerWelcome.adapter = FragmentsViewPagerManager(this)

        binding.viewPagerWelcome.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.textViewPrevious.visibility = if (position > 0) View.VISIBLE else View.GONE

                binding.textViewNext.visibility = if (position < 2) View.VISIBLE else View.GONE

                super.onPageSelected(position)
            }
        })

        TabLayoutMediator(binding.tabLayoutWelcome, binding.viewPagerWelcome) { _, _ -> }.attach()
    }

    private fun setClickListeners() {
        binding.textViewNext.setOnClickListener {
            binding.viewPagerWelcome.currentItem++
        }

        binding.textViewPrevious.setOnClickListener {
            binding.viewPagerWelcome.currentItem--
        }
    }
}