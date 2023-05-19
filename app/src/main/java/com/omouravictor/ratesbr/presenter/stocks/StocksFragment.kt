package com.omouravictor.ratesbr.presenter.stocks

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.ratesbr.R
import com.omouravictor.ratesbr.databinding.FragmentStocksBinding
import com.omouravictor.ratesbr.presenter.base.UiResultStatus
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
import com.omouravictor.ratesbr.util.FormatUtils.BrazilianFormats.brDateFormat
import com.omouravictor.ratesbr.util.FormatUtils.BrazilianFormats.brNumberFormat
import com.omouravictor.ratesbr.util.FormatUtils.BrazilianFormats.brTimeFormat
import com.omouravictor.ratesbr.util.OptionsMenuUtils.addOptionsMenu
import com.omouravictor.ratesbr.util.OptionsMenuUtils.searchMenuItem
import com.omouravictor.ratesbr.util.StringUtils.getVariationText

class StocksFragment : Fragment() {

    private lateinit var stockDetailsDialog: Dialog
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

        initOptionsMenu()
        initStockDetailsDialog()
        configSwipeRefreshLayout()

        stockViewModel.stocksResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiResultStatus.Success -> {
                    configRecyclerView(result.data)
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.recyclerViewStocks.isVisible = true
                    binding.includeViewError.root.isVisible = false
                }
                is UiResultStatus.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.recyclerViewStocks.isVisible = false
                    binding.includeViewError.root.isVisible = true
                    binding.includeViewError.textViewErrorMessage.text = result.e.message
                }
                is UiResultStatus.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                    binding.recyclerViewStocks.isVisible = false
                    binding.includeViewError.root.isVisible = false
                }
            }
        }
    }

    private fun initOptionsMenu() {
        addOptionsMenu(requireActivity(), viewLifecycleOwner) { text ->
            (binding.recyclerViewStocks.adapter as? StocksAdapter)?.filterList(text)
        }
    }

    private fun initStockDetailsDialog() {
        stockDetailsDialog = Dialog(requireContext())
        stockDetailsDialog.setContentView(R.layout.details_stock_dialog)
        stockDetailsDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        stockDetailsDialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
    }

    private fun configSwipeRefreshLayout() {
        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)
        binding.swipeRefreshLayout.setColorSchemeColors(greenColor, greenColor, greenColor)
        binding.swipeRefreshLayout.setOnRefreshListener {
            (searchMenuItem.actionView as SearchView).onActionViewCollapsed()
            stockViewModel.getStocks()
        }
    }

    private fun configRecyclerView(stockList: List<StockUiModel>) {
        binding.recyclerViewStocks.apply {
            adapter = StocksAdapter(stockList) { showStockDetailsDialog(it) }
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showStockDetailsDialog(stockUiModel: StockUiModel) {
        with(stockDetailsDialog) {
            val nameTextView = findViewById<TextView>(R.id.textViewStockPopupName)
            val fullNameTextView = findViewById<TextView>(R.id.textViewStockPopupFullName)
            val locationTextView = findViewById<TextView>(R.id.textViewStockPopupLocation)
            val pointsTextView = findViewById<TextView>(R.id.textViewStockPopupPoints)
            val variationTextView = findViewById<TextView>(R.id.textViewStockPopupVariation)
            val dateTimeTextView = findViewById<TextView>(R.id.textViewStockPopupDateTime)

            nameTextView.text = stockUiModel.name
            fullNameTextView.text = stockUiModel.fullName
            locationTextView.text = getString(
                R.string.stock_popup_full_location,
                stockUiModel.cityLocation,
                stockUiModel.countryLocation
            )
            pointsTextView.text = brNumberFormat.format(stockUiModel.points)
            variationTextView.text = getVariationText(stockUiModel.variation)
            dateTimeTextView.text = getString(
                R.string.popup_date_time,
                brDateFormat.format(stockUiModel.stockDate),
                brTimeFormat.format(stockUiModel.stockDate)
            )

            show()
        }
    }
}