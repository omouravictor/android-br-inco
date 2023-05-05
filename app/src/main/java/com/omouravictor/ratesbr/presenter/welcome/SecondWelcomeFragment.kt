package com.omouravictor.ratesbr.presenter.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.omouravictor.ratesbr.databinding.FragmentWelcomeSecondBinding

class SecondWelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeSecondBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeSecondBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}