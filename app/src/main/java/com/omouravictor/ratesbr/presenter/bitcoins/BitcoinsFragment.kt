package com.omouravictor.ratesbr.presenter.bitcoins

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.ratesbr.R
import com.omouravictor.ratesbr.databinding.FragmentBitcoinsBinding
import com.omouravictor.ratesbr.presenter.base.UiResultStatus
import com.omouravictor.ratesbr.presenter.bitcoins.model.BitcoinUiModel
import com.omouravictor.ratesbr.util.FormatUtils.BrazilianFormats.brDateFormat
import com.omouravictor.ratesbr.util.FormatUtils.BrazilianFormats.brTimeFormat
import com.omouravictor.ratesbr.util.FormatUtils.getFormattedValueForCurrencyLocale
import com.omouravictor.ratesbr.presenter.base.OptionsMenu.addOptionsMenu
import com.omouravictor.ratesbr.util.StringUtils.getVariationText
import java.util.Locale

class BitcoinsFragment : Fragment() {

    private lateinit var bitcoinDetailsDialog: Dialog
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

        initOptionsMenu()
        initBitcoinDetailsDialog()

        bitcoinViewModel.bitcoinsResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiResultStatus.Success -> {
                    configRecyclerView(result.data)
                    binding.progressBar.isVisible = false
                    binding.recyclerViewBitcoins.isVisible = true
                    binding.includeViewError.root.isVisible = false
                }

                is UiResultStatus.Error -> {
                    binding.progressBar.isVisible = false
                    binding.recyclerViewBitcoins.isVisible = false
                    binding.includeViewError.root.isVisible = true
                    binding.includeViewError.textViewErrorMessage.text = result.e.message
                }

                is UiResultStatus.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.recyclerViewBitcoins.isVisible = false
                    binding.includeViewError.root.isVisible = false
                }
            }
        }
    }

    private fun initOptionsMenu() {
        addOptionsMenu(requireActivity(), viewLifecycleOwner) { text ->
            (binding.recyclerViewBitcoins.adapter as? BitcoinsAdapter)?.filterList(text)
        }
    }

    private fun initBitcoinDetailsDialog() {
        bitcoinDetailsDialog = Dialog(requireContext())
        bitcoinDetailsDialog.setContentView(R.layout.details_bitcoin_dialog)
        bitcoinDetailsDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        bitcoinDetailsDialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
    }

    private fun configRecyclerView(bitcoinList: List<BitcoinUiModel>) {
        binding.recyclerViewBitcoins.apply {
            adapter = BitcoinsAdapter(bitcoinList) { showBitcoinDetailsDialog(it) }
            layoutManager = LinearLayoutManager(context)
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