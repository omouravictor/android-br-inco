package com.omouravictor.br_inco.presenter.bitcoins

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
import com.omouravictor.br_inco.presenter.base.SearchOptionMenu
import com.omouravictor.br_inco.presenter.base.UiResultStatus
import com.omouravictor.br_inco.presenter.bitcoins.model.BitcoinUiModel
import com.omouravictor.br_inco.util.FormatUtils
import com.omouravictor.br_inco.util.StringUtils
import java.util.Locale

class BitcoinsFragment : Fragment() {

    private lateinit var bitcoinDetailsDialog: Dialog
    private lateinit var binding: FragmentInfoCardsBinding
    private val searchOptionMenu = SearchOptionMenu()
    private val bitcoinsAdapter = BitcoinsAdapter { showBitcoinDetailsDialog(it) }
    private val bitcoinViewModel: BitcoinsViewModel by activityViewModels()

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
        initBitcoinDetailsDialog()
        initRecyclerView()
        initSwipeRefreshLayout()

        observeStocksResult()
    }

    override fun onResume() {
        super.onResume()
        bitcoinsAdapter.clearFilter()
    }

    private fun observeStocksResult() {
        bitcoinViewModel.bitcoinsResult.observe(viewLifecycleOwner) { result ->
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

    private fun handleUiSuccessResult(data: Pair<List<BitcoinUiModel>, DataSource>) {
        setupViews(
            swipeRefreshLayoutIsRefreshing = false,
            recyclerViewIsVisible = true,
            viewErrorIsVisible = false
        )

        bitcoinsAdapter.setList(data.first)
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
        searchOptionMenu.addSearchOptionMenu(requireActivity(), viewLifecycleOwner) { text ->
            bitcoinsAdapter.filterList(text)
        }
    }

    private fun initBitcoinDetailsDialog() {
        bitcoinDetailsDialog = Dialog(requireContext())
        bitcoinDetailsDialog.setContentView(R.layout.details_bitcoin_dialog)
        bitcoinDetailsDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        bitcoinDetailsDialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        bitcoinDetailsDialog.window?.attributes?.windowAnimations =
            R.style.Animation_Design_BottomSheetDialog
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = bitcoinsAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun initSwipeRefreshLayout() {
        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)
        binding.swipeRefreshLayout.setColorSchemeColors(greenColor, greenColor, greenColor)
        binding.swipeRefreshLayout.setOnRefreshListener {
            (searchOptionMenu.searchMenuItem.actionView as SearchView).onActionViewCollapsed()
            bitcoinViewModel.getBitcoins()
        }
    }

    private fun showBitcoinDetailsDialog(bitcoin: BitcoinUiModel) {
        with(bitcoinDetailsDialog) {
            val bitcoinLocale = Locale(bitcoin.language, bitcoin.countryLanguage)
            val nameTextView = findViewById<TextView>(R.id.textViewBitcoinPopupName)
            val currencyBuyTextView = findViewById<TextView>(R.id.textViewBitcoinPopupCurrencyBuy)
            val unitaryValueTextView = findViewById<TextView>(R.id.textViewBitcoinPopupUnitaryValue)
            val variationTextView = findViewById<TextView>(R.id.textViewBitcoinPopupVariation)
            val dateTimeTextView = findViewById<TextView>(R.id.textViewBitcoinPopupDateTime)

            nameTextView.text = bitcoin.name
            currencyBuyTextView.text = getString(
                R.string.popup_currency_buy,
                bitcoin.currencyTerm,
                bitcoin.currencyName
            )
            unitaryValueTextView.text = FormatUtils.getFormattedValueForCurrencyLocale(
                bitcoin.unitaryRate,
                bitcoinLocale
            )
            variationTextView.text = StringUtils.getVariationText(bitcoin.variation)
            dateTimeTextView.text = getString(
                R.string.popup_date_time,
                FormatUtils.BrazilianFormats.brDateFormat.format(bitcoin.bitcoinDate),
                FormatUtils.BrazilianFormats.brTimeFormat.format(bitcoin.bitcoinDate)
            )

            show()
        }
    }

}