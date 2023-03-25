package com.omouravictor.ratesbr.presenter.welcome

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentsViewPagerManager(fm: FragmentActivity) : FragmentStateAdapter(fm) {

    private val fragments: List<Fragment> =
        listOf(FirstWelcomeFragment(), SecondWelcomeFragment(), ThirdWelcomeFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
