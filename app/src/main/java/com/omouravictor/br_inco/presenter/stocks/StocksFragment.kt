package com.omouravictor.br_inco.presenter.stocks

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AnimationUtils.loadLayoutAnimation
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.br_inco.R
import com.omouravictor.br_inco.databinding.FragmentInfoCardsBinding
import com.omouravictor.br_inco.presenter.base.DataSource
import com.omouravictor.br_inco.presenter.base.OptionsMenu
import com.omouravictor.br_inco.presenter.base.UiResultStatus
import com.omouravictor.br_inco.presenter.stocks.model.StockUiModel
import com.omouravictor.br_inco.util.FormatUtils
import com.omouravictor.br_inco.util.StringUtils

class StocksFragment : Fragment() {

    private lateinit var stockDetailsDialog: Dialog
    private lateinit var binding: FragmentInfoCardsBinding
    private val optionsMenu = OptionsMenu()
    private val stocksAdapter = StocksAdapter { showStockDetailsDialog(it) }
    private val stockViewModel: StocksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoCardsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOptionsMenu()
        initStockDetailsDialog()
        initRecyclerView()
        initSwipeRefreshLayout()

        observeStocksResult()
    }

    override fun onResume() {
        super.onResume()
        stocksAdapter.clearFilter()
    }

    private fun observeStocksResult() {
        stockViewModel.stocksResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiResultStatus.Success -> handleUiSuccessResult(result.data)
                is UiResultStatus.Error -> handleUiErrorResult(result.message)
                is UiResultStatus.Loading -> handleUiLoadingResult()
            }
        }
    }

    private fun setupViews(
        swipeRefreshLayoutIsRefreshing: Boolean,
        recyclerViewIsVisible: Boolean,
        viewErrorIsVisible: Boolean
    ) {
        binding.swipeRefreshLayout.isRefreshing = swipeRefreshLayoutIsRefreshing
        binding.recyclerView.isVisible = recyclerViewIsVisible
        binding.includeViewError.root.isVisible = viewErrorIsVisible
    }

    private fun handleUiSuccessResult(data: Pair<List<StockUiModel>, DataSource>) {
        setupViews(
            swipeRefreshLayoutIsRefreshing = false,
            recyclerViewIsVisible = true,
            viewErrorIsVisible = false
        )

        stocksAdapter.setList(data.first)
        binding.recyclerView.layoutAnimation = loadLayoutAnimation(context, R.anim.layout_animation)
        binding.swipeRefreshLayout.isEnabled = data.second == DataSource.LOCAL
    }

    private fun handleUiErrorResult(message: String) {
        setupViews(
            swipeRefreshLayoutIsRefreshing = false,
            recyclerViewIsVisible = false,
            viewErrorIsVisible = true
        )

        binding.includeViewError.textViewErrorMessage.text = message
    }

    private fun handleUiLoadingResult() {
        setupViews(
            swipeRefreshLayoutIsRefreshing = true,
            recyclerViewIsVisible = false,
            viewErrorIsVisible = false
        )
    }

    private fun initOptionsMenu() {
        optionsMenu.addOptionsMenu(requireActivity(), viewLifecycleOwner) { text ->
            stocksAdapter.filterList(text)
        }
    }

    private fun initStockDetailsDialog() {
        stockDetailsDialog = Dialog(requireContext())
        stockDetailsDialog.setContentView(R.layout.details_stock_dialog)
        stockDetailsDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        stockDetailsDialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        stockDetailsDialog.window?.attributes?.windowAnimations =
            R.style.Animation_Design_BottomSheetDialog
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = stocksAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun initSwipeRefreshLayout() {
        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)
        binding.swipeRefreshLayout.setColorSchemeColors(greenColor, greenColor, greenColor)
        binding.swipeRefreshLayout.setOnRefreshListener {
            (optionsMenu.searchMenuItem.actionView as SearchView).onActionViewCollapsed()
            stockViewModel.getStocks()
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
            pointsTextView.text =
                FormatUtils.BrazilianFormats.brDecimalFormat.format(stockUiModel.points)
            variationTextView.text = StringUtils.getVariationText(stockUiModel.variation)
            dateTimeTextView.text = getString(
                R.string.popup_date_time,
                FormatUtils.BrazilianFormats.brDateFormat.format(stockUiModel.stockDate),
                FormatUtils.BrazilianFormats.brTimeFormat.format(stockUiModel.stockDate)
            )

            show()
        }
    }
}