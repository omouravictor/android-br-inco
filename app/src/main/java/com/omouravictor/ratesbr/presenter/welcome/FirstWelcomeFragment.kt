package com.omouravictor.ratesbr.presenter.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.omouravictor.ratesbr.databinding.FragmentWelcomeFirstBinding

class FirstWelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeFirstBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}