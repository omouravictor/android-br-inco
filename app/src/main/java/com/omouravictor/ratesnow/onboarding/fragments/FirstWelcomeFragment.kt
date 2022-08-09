package com.omouravictor.ratesnow.onboarding.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.omouravictor.ratesnow.databinding.FragmentFirstWelcomeBinding

class FirstWelcomeFragment : Fragment() {

    private lateinit var binding: FragmentFirstWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstWelcomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}