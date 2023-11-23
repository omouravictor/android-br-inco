package com.omouravictor.br_inco.presenter.bitcoins

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
import com.omouravictor.br_inco.R
import com.omouravictor.br_inco.databinding.FragmentBitcoinsBinding
import com.omouravictor.br_inco.presenter.base.DataSource
import com.omouravictor.br_inco.presenter.base.OptionsMenu
import com.omouravictor.br_inco.presenter.base.UiResultStatus
import com.omouravictor.br_inco.presenter.bitcoins.model.BitcoinUiModel
import com.omouravictor.br_inco.util.FormatUtils.BrazilianFormats.brDateFormat
import com.omouravictor.br_inco.util.FormatUtils.BrazilianFormats.brTimeFormat
import com.omouravictor.br_inco.util.FormatUtils.getFormattedValueForCurrencyLocale
import com.omouravictor.br_inco.util.StringUtils.getVariationText
import java.util.Locale

class BitcoinsFragment : Fragment() {

    private lateinit var bitcoinDetailsDialog: Dialog
    private lateinit var binding: FragmentBitcoinsBinding
    private val optionsMenu = OptionsMenu()
    private val bitcoinViewModel: BitcoinsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBitcoinsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOptionsMenu()
        initBitcoinDetailsDialog()
        initSwipeRefreshLayout()

        observeStocksResult()
    }

    override fun onResume() {
        super.onResume()
        (binding.recyclerViewBitcoins.adapter as? BitcoinsAdapter)?.filterList("")
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

    private fun handleUiSuccessResult(data: Pair<List<BitcoinUiModel>, DataSource>) {
        configRecyclerView(data.first)
        configSwipeRefreshLayout(data.second)
        binding.swipeRefreshLayout.isRefreshing = false
        binding.recyclerViewBitcoins.isVisible = true
        binding.includeViewError.root.isVisible = false
    }

    private fun handleUiErrorResult(message: String) {
        binding.swipeRefreshLayout.isRefreshing = false
        binding.recyclerViewBitcoins.isVisible = false
        binding.includeViewError.root.isVisible = true
        binding.includeViewError.textViewErrorMessage.text = message
    }

    private fun handleUiLoadingResult() {
        binding.swipeRefreshLayout.isRefreshing = true
        binding.recyclerViewBitcoins.isVisible = false
        binding.includeViewError.root.isVisible = false
    }

    private fun initOptionsMenu() {
        optionsMenu.addOptionsMenu(requireActivity(), viewLifecycleOwner) { text ->
            (binding.recyclerViewBitcoins.adapter as? BitcoinsAdapter)?.filterList(text)
        }
    }

    private fun initBitcoinDetailsDialog() {
        bitcoinDetailsDialog = Dialog(requireContext())
        bitcoinDetailsDialog.setContentView(R.layout.details_bitcoin_dialog)
        bitcoinDetailsDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        bitcoinDetailsDialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
    }

    private fun initSwipeRefreshLayout() {
        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)
        binding.swipeRefreshLayout.setColorSchemeColors(greenColor, greenColor, greenColor)
        binding.swipeRefreshLayout.setOnRefreshListener {
            (optionsMenu.searchMenuItem.actionView as SearchView).onActionViewCollapsed()
            bitcoinViewModel.getBitcoins()
        }
    }

    private fun configRecyclerView(bitcoinList: List<BitcoinUiModel>) {
        binding.recyclerViewBitcoins.apply {
            adapter = BitcoinsAdapter(bitcoinList) { showBitcoinDetailsDialog(it) }
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun configSwipeRefreshLayout(dataSource: DataSource) {
        binding.swipeRefreshLayout.isEnabled = dataSource == DataSource.LOCAL
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
            unitaryValueTextView.text = getFormattedValueForCurrencyLocale(
                bitcoin.unitaryRate,
                bitcoinLocale
            )
            variationTextView.text = getVariationText(bitcoin.variation)
            dateTimeTextView.text = getString(
                R.string.popup_date_time,
                brDateFormat.format(bitcoin.bitcoinDate),
                brTimeFormat.format(bitcoin.bitcoinDate)
            )

            show()
        }
    }

}