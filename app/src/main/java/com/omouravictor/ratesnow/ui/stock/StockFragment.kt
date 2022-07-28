package com.omouravictor.ratesnow.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.ratesnow.adapter.StockAdapter
import com.omouravictor.ratesnow.databinding.FragmentStockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StockFragment : Fragment() {

    private lateinit var binding: FragmentStockBinding
    private val viewModel: StockViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStockBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initSwipeRefreshLayout()
        initStocksRecyclerView()

        viewModel.getStocksFromApi()

        lifecycleScope.launchWhenStarted {
            viewModel.stocks.collect { event ->
                when (event) {
                    is StockViewModel.StockEvent.Success -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.progressBar.isVisible = false
                        binding.rvStocks.isVisible = true
                        (binding.rvStocks.adapter as StockAdapter).setList(event.stocksList)
                    }
                    is StockViewModel.StockEvent.Failure -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.progressBar.isVisible = false
                        binding.rvStocks.isVisible = false
                        Toast.makeText(context, event.errorText, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is StockViewModel.StockEvent.Loading -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.progressBar.isVisible = true
                        binding.rvStocks.isVisible = false
                    }
                    is StockViewModel.StockEvent.Empty -> {
                        binding.rvStocks.isVisible = false
                    }
                }
            }
        }
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.getStocksFromApi() }
    }

    private fun initStocksRecyclerView() {
        binding.rvStocks.apply {
            adapter = StockAdapter(mutableListOf())
            layoutManager = LinearLayoutManager(context)
        }
    }
}