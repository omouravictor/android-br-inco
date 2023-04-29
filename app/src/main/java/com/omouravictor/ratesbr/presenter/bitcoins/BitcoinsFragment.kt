package com.omouravictor.ratesbr.presenter.bitcoins

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
import com.omouravictor.ratesbr.databinding.FragmentBitcoinsBinding
import com.omouravictor.ratesbr.presenter.base.UiResultState
import com.omouravictor.ratesbr.presenter.bitcoins.model.BitcoinUiModel
import com.omouravictor.ratesbr.util.BrazilianFormats.dateFormat
import com.omouravictor.ratesbr.util.BrazilianFormats.timeFormat
import com.omouravictor.ratesbr.util.Formats.getFormattedValueForCurrencyLocale
import com.omouravictor.ratesbr.util.StringUtils.getVariationText
import java.util.*

class BitcoinsFragment : Fragment() {

    private lateinit var dialog: Dialog
    private lateinit var binding: FragmentBitcoinsBinding
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

        initBitcoinDialog()
        initSwipeRefreshLayout()

        bitcoinViewModel.bitcoinsResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiResultState.Success -> {
                    configureRecyclerView(result.data)
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.recyclerViewBitcoins.isVisible = true
                    binding.includeViewError.root.isVisible = false
                }
                is UiResultState.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.recyclerViewBitcoins.isVisible = false
                    binding.includeViewError.root.isVisible = true
                    binding.includeViewError.textViewErrorMessage.text = result.e.message
                }
                is UiResultState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                    binding.recyclerViewBitcoins.isVisible = false
                    binding.includeViewError.root.isVisible = false
                }
            }
        }
    }

    private fun initBitcoinDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.bitcoin_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
    }

    private fun initSwipeRefreshLayout() {
        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)
        binding.swipeRefreshLayout.setColorSchemeColors(greenColor, greenColor, greenColor)
        binding.swipeRefreshLayout.setOnRefreshListener { bitcoinViewModel.getBitcoins() }
    }

    private fun configureRecyclerView(bitcoinList: List<BitcoinUiModel>) {
        binding.recyclerViewBitcoins.apply {
            adapter = BitcoinsAdapter(bitcoinList) { bitcoinsAdapterOnClickItem(it) }
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun bitcoinsAdapterOnClickItem(bitcoin: BitcoinUiModel) {
        with(dialog) {
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
                dateFormat.format(bitcoin.bitcoinDate),
                timeFormat.format(bitcoin.bitcoinDate)
            )

            show()
        }
    }

}