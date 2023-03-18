package com.omouravictor.ratesnow.ui.view.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.omouravictor.ratesnow.ui.view.fragment.FirstWelcomeFragment
import com.omouravictor.ratesnow.ui.view.fragment.SecondWelcomeFragment
import com.omouravictor.ratesnow.ui.view.fragment.ThirdWelcomeFragment

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
