package com.omouravictor.ratesnow.presenter.bitcoins

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.ratesnow.databinding.FragmentBitcoinBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BitCoinFragment : Fragment() {

    private lateinit var binding: FragmentBitcoinBinding
    private val viewModel: BitCoinViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBitcoinBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initSwipeRefreshLayout()
        initRecyclerView()

        lifecycleScope.launchWhenStarted {
            viewModel.bitCoins.collect { event ->
                when (event) {
                    is BitCoinViewModel.BitCoinEvent.Success -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.progressBar.isVisible = false
                        binding.rvBitCoins.isVisible = true
                    }
                    is BitCoinViewModel.BitCoinEvent.Failure -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.progressBar.isVisible = false
                        binding.rvBitCoins.isVisible = true
                        Toast.makeText(context, event.errorText, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is BitCoinViewModel.BitCoinEvent.Loading -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.progressBar.isVisible = true
                        binding.rvBitCoins.isVisible = false
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBitCoin()
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getBitCoin()
        }
    }

    private fun initRecyclerView() {
        viewModel.bitCoinsList.observe(this, Observer {
            binding.rvBitCoins.apply {
                adapter = BitCoinAdapter(it, context)
                layoutManager = LinearLayoutManager(context)
            }
        })
    }
}