package com.omouravictor.ratesbr.presenter.rates

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.SearchView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.omouravictor.ratesbr.R
import com.omouravictor.ratesbr.databinding.FragmentRatesBinding
import com.omouravictor.ratesbr.presenter.base.DataSource
import com.omouravictor.ratesbr.presenter.base.OptionsMenu
import com.omouravictor.ratesbr.presenter.base.OptionsMenu.addOptionsMenu
import com.omouravictor.ratesbr.presenter.base.UiResultStatus
import com.omouravictor.ratesbr.presenter.converter.ConverterViewModel
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import com.omouravictor.ratesbr.util.FormatUtils.BrazilianFormats.brCurrencyFormat
import com.omouravictor.ratesbr.util.FormatUtils.BrazilianFormats.brDateFormat
import com.omouravictor.ratesbr.util.FormatUtils.BrazilianFormats.brTimeFormat
import com.omouravictor.ratesbr.util.StringUtils.getVariationText

class RatesFragment : Fragment() {

    private lateinit var rateBottomSheetDialog: BottomSheetDialog
    private lateinit var rateDetailsDialog: Dialog
    private lateinit var binding: FragmentRatesBinding
    private val ratesViewModel: RatesViewModel by activityViewModels()
    private val converterViewModel: ConverterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRatesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOptionsMenu()
        initRateBottomSheetDialog()
        initRateDetailsDialog()
        initSwipeRefreshLayout()

        observeRatesResult()
    }

    private fun observeRatesResult() {
        ratesViewModel.ratesResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiResultStatus.Success -> handleUiSuccessResult(result.data)
                is UiResultStatus.Error -> handleUiErrorResult(result.message)
                is UiResultStatus.Loading -> handleUiLoadingResult()
            }
        }
    }

    private fun handleUiSuccessResult(data: Pair<List<RateUiModel>, DataSource>) {
        configRecyclerView(data.first)
        configSwipeRefreshLayout(data.second)
        binding.swipeRefreshLayout.isRefreshing = false
        binding.recyclerViewRates.isVisible = true
        binding.includeViewError.root.isVisible = false
    }

    private fun handleUiErrorResult(message: String) {
        binding.swipeRefreshLayout.isRefreshing = false
        binding.recyclerViewRates.isVisible = false
        binding.includeViewError.root.isVisible = true
        binding.includeViewError.textViewErrorMessage.text = message
    }

    private fun handleUiLoadingResult() {
        binding.swipeRefreshLayout.isRefreshing = true
        binding.recyclerViewRates.isVisible = false
        binding.includeViewError.root.isVisible = false
    }

    private fun initOptionsMenu() {
        addOptionsMenu(requireActivity(), viewLifecycleOwner) { text ->
            (binding.recyclerViewRates.adapter as? RatesAdapter)?.filterList(text)
        }
    }

    private fun initRateBottomSheetDialog() {
        rateBottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.OverlayBottomSheetDialog)
        rateBottomSheetDialog.setContentView(R.layout.bottom_sheet_rate_dialog)
        rateBottomSheetDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun initRateDetailsDialog() {
        rateDetailsDialog = Dialog(requireContext())
        rateDetailsDialog.setContentView(R.layout.details_rate_dialog)
        rateDetailsDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        rateDetailsDialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
    }

    private fun initSwipeRefreshLayout() {
        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)
        binding.swipeRefreshLayout.setColorSchemeColors(greenColor, greenColor, greenColor)
        binding.swipeRefreshLayout.setOnRefreshListener {
            (OptionsMenu.searchMenuItem.actionView as SearchView).onActionViewCollapsed()
            ratesViewModel.getRates()
        }
    }

    private fun configRecyclerView(ratesList: List<RateUiModel>) {
        binding.recyclerViewRates.apply {
            adapter = RatesAdapter(ratesList) { showRateBottomSheetDialog(it) }
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun configSwipeRefreshLayout(dataSource: DataSource) {
        binding.swipeRefreshLayout.isEnabled = dataSource == DataSource.LOCAL
    }

    private fun showRateBottomSheetDialog(rateUiModel: RateUiModel) {
        with(rateBottomSheetDialog) {
            val textViewCurrencyName =
                findViewById<TextView>(R.id.textViewCurrencyNameBottomSheetRateDialog)
            val layoutConverter =
                findViewById<ConstraintLayout>(R.id.converterLayoutBottomSheetRateDialog)
            val layoutDetails =
                findViewById<ConstraintLayout>(R.id.detailsLayoutBottomSheetRateDialog)

            textViewCurrencyName!!.text = rateUiModel.currencyName

            layoutConverter!!.setOnClickListener {
                rateBottomSheetDialog.dismiss()
                prepareAndNavigateToConverterFragment(rateUiModel)
            }

            layoutDetails!!.setOnClickListener {
                rateBottomSheetDialog.dismiss()
                showRateDetailsDialog(rateUiModel)
            }

            show()
        }
    }

    private fun prepareAndNavigateToConverterFragment(rateUiModel: RateUiModel) {
        converterViewModel.setInitialValues(rateUiModel)
        val action = RatesFragmentDirections.actionRatesFragmentToConverterFragment()
        findNavController().navigate(action)
    }

    private fun showRateDetailsDialog(rate: RateUiModel) {
        with(rateDetailsDialog) {
            val nameTextView = findViewById<TextView>(R.id.textViewRatePopupName)
            val currencyTermTextView = findViewById<TextView>(R.id.textViewRatePopupCurrencyTerm)
            val unitaryValueTextView = findViewById<TextView>(R.id.textViewRatePopupUnitaryValue)
            val variationTextView = findViewById<TextView>(R.id.textViewRatePopupVariation)
            val dateTimeTextView = findViewById<TextView>(R.id.textViewRatePopupDateTime)

            nameTextView.text = rate.currencyName
            currencyTermTextView.text = rate.currencyTerm
            unitaryValueTextView.text = brCurrencyFormat.format(rate.unitaryRate)
            variationTextView.text = getVariationText(rate.variation)
            dateTimeTextView.text = getString(
                R.string.popup_date_time,
                brDateFormat.format(rate.rateDate),
                brTimeFormat.format(rate.rateDate)
            )

            show()
        }
    }

}