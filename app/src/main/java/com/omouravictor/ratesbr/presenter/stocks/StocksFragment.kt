package com.omouravictor.ratesbr.presenter.stocks

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.ratesbr.R
import com.omouravictor.ratesbr.databinding.FragmentStocksBinding
import com.omouravictor.ratesbr.presenter.base.UiResultState
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
import com.omouravictor.ratesbr.util.BrazilianFormats.dateFormat
import com.omouravictor.ratesbr.util.BrazilianFormats.numberFormat
import com.omouravictor.ratesbr.util.BrazilianFormats.timeFormat
import com.omouravictor.ratesbr.util.Functions.getVariationText

class StocksFragment : Fragment() {

    private lateinit var dialog: Dialog
    private lateinit var binding: FragmentStocksBinding
    private val stockViewModel: StocksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStocksBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDialog()
        initSwipeRefreshLayout()

        stockViewModel.stocksResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiResultState.Success -> {
                    configureRecyclerView(result.data)
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.recyclerViewStocks.isVisible = true
                    binding.includeViewError.root.isVisible = false
                }
                is UiResultState.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.recyclerViewStocks.isVisible = false
                    binding.includeViewError.root.isVisible = true
                    binding.includeViewError.textViewErrorMessage.text = result.e.message
                }
                is UiResultState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                    binding.recyclerViewStocks.isVisible = false
                    binding.includeViewError.root.isVisible = false
                }
            }
        }
    }

    private fun initDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.stock_popup)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
    }

    private fun initSwipeRefreshLayout() {
        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)
        binding.swipeRefreshLayout.setColorSchemeColors(greenColor, greenColor, greenColor)
        binding.swipeRefreshLayout.setOnRefreshListener { stockViewModel.getStocks() }
    }

    private fun configureRecyclerView(stockList: List<StockUiModel>) {
        binding.recyclerViewStocks.apply {
            adapter = StocksAdapter(stockList) { stocksAdapterOnClickItem(it) }
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun stocksAdapterOnClickItem(stockUiModel: StockUiModel) {
        with(dialog) {
            val stockNameTextView = findViewById<TextView>(R.id.textViewStockPopupName)
            val stockFullNameTextView = findViewById<TextView>(R.id.textViewStockPopupFullName)
            val stockLocationTextView = findViewById<TextView>(R.id.textViewStockPopupLocation)
            val stockPointsTextView = findViewById<TextView>(R.id.textViewStockPopupPoints)
            val stockVariationTextView = findViewById<TextView>(R.id.textViewStockPopupVariation)
            val stockDateTimeTextView = findViewById<TextView>(R.id.textViewStockPopupDateTime)

            stockNameTextView.text = stockUiModel.name
            stockFullNameTextView.text = stockUiModel.fullName
            stockLocationTextView.text = stockUiModel.location
            stockPointsTextView.text = numberFormat.format(stockUiModel.points)
            stockVariationTextView.text = getVariationText(stockUiModel.variation)
            stockDateTimeTextView.text = getString(
                R.string.stock_popup_date_time,
                dateFormat.format(stockUiModel.stockDate),
                timeFormat.format(stockUiModel.stockDate)
            )

            show()
        }
    }
}