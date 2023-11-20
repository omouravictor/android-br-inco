package com.omouravictor.brinco.presenter.welcome

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentsViewPagerManager(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    private val fragments: List<Fragment> =
        listOf(FirstWelcomeFragment(), SecondWelcomeFragment(), ThirdWelcomeFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
