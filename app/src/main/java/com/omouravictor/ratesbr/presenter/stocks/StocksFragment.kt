package com.omouravictor.ratesbr.presenter.stocks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.ratesbr.databinding.FragmentStocksBinding
import com.omouravictor.ratesbr.presenter.base.UiResultState
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
import dagger.hilt.android.AndroidEntryPoint

class StocksFragment : Fragment() {

    private lateinit var stockBinding: FragmentStocksBinding
    private val stockViewModel: StocksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        stockBinding = FragmentStocksBinding.inflate(layoutInflater, container, false)
        return stockBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSwipeRefreshLayout()

        stockViewModel.stocks.observe(viewLifecycleOwner) {
            when (it) {
                is UiResultState.Success -> {
                    configureRecyclerView(it.data)
                    stockBinding.swipeRefreshLayout.isRefreshing = false
                    stockBinding.progressBar.isVisible = false
                    stockBinding.rvStocks.isVisible = true
                    stockBinding.includeViewError.root.isVisible = false
                }
                is UiResultState.Error -> {
                    stockBinding.swipeRefreshLayout.isRefreshing = false
                    stockBinding.progressBar.isVisible = false
                    stockBinding.rvStocks.isVisible = false
                    stockBinding.includeViewError.root.isVisible = true
                    stockBinding.includeViewError.textViewErrorMessage.text = it.e.message
                }
                is UiResultState.Loading -> {
                    stockBinding.swipeRefreshLayout.isRefreshing = false
                    stockBinding.progressBar.isVisible = true
                    stockBinding.rvStocks.isVisible = false
                    stockBinding.includeViewError.root.isVisible = false
                }
            }
        }
    }

    private fun initSwipeRefreshLayout() {
        stockBinding.swipeRefreshLayout.setOnRefreshListener {
            stockViewModel.getStocks()
        }
    }

    private fun configureRecyclerView(stockList: List<StockUiModel>) {
        stockBinding.rvStocks.apply {
            adapter = StocksAdapter(stockList)
            layoutManager = LinearLayoutManager(context)
        }
    }

}