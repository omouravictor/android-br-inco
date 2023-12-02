package com.omouravictor.br_inco.presenter.rates

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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.omouravictor.br_inco.R
import com.omouravictor.br_inco.databinding.FragmentInfoCardsBinding
import com.omouravictor.br_inco.presenter.base.DataSource
import com.omouravictor.br_inco.presenter.base.OptionsMenu
import com.omouravictor.br_inco.presenter.base.UiResultStatus
import com.omouravictor.br_inco.presenter.converter.ConverterViewModel
import com.omouravictor.br_inco.presenter.rates.model.RateUiModel
import com.omouravictor.br_inco.util.FormatUtils
import com.omouravictor.br_inco.util.StringUtils

class RatesFragment : Fragment() {

    private lateinit var rateBottomSheetDialog: BottomSheetDialog
    private lateinit var rateDetailsDialog: Dialog
    private lateinit var binding: FragmentInfoCardsBinding
    private val ratesAdapter = RatesAdapter { showRateBottomSheetDialog(it) }
    private val optionsMenu = OptionsMenu()
    private val ratesViewModel: RatesViewModel by activityViewModels()
    private val converterViewModel: ConverterViewModel by activityViewModels()

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
        initRateBottomSheetDialog()
        initRateDetailsDialog()
        initRecyclerView()
        initSwipeRefreshLayout()

        observeRatesResult()
    }

    override fun onResume() {
        super.onResume()
        ratesAdapter.clearFilter()
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

    private fun setupViews(
        swipeRefreshLayoutIsRefreshing: Boolean,
        recyclerViewIsVisible: Boolean,
        viewErrorIsVisible: Boolean
    ) {
        binding.swipeRefreshLayout.isRefreshing = swipeRefreshLayoutIsRefreshing
        binding.recyclerView.isVisible = recyclerViewIsVisible
        binding.includeViewError.root.isVisible = viewErrorIsVisible
    }

    private fun handleUiSuccessResult(data: Pair<List<RateUiModel>, DataSource>) {
        setupViews(
            swipeRefreshLayoutIsRefreshing = false,
            recyclerViewIsVisible = true,
            viewErrorIsVisible = false
        )

        ratesAdapter.setList(data.first)
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
            ratesAdapter.filterList(text)
        }
    }

    private fun initRateBottomSheetDialog() {
        rateBottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.Theme_App_OverlayBottomSheetDialog)
        rateBottomSheetDialog.setContentView(R.layout.bottom_sheet_rate_dialog)
        rateBottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun initRateDetailsDialog() {
        rateDetailsDialog = Dialog(requireContext())
        rateDetailsDialog.setContentView(R.layout.details_rate_dialog)
        rateDetailsDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        rateDetailsDialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        rateDetailsDialog.window?.attributes?.windowAnimations =
            R.style.Animation_Design_BottomSheetDialog
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = ratesAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun initSwipeRefreshLayout() {
        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)
        binding.swipeRefreshLayout.setColorSchemeColors(greenColor, greenColor, greenColor)
        binding.swipeRefreshLayout.setOnRefreshListener {
            (optionsMenu.searchMenuItem.actionView as SearchView).onActionViewCollapsed()
            ratesViewModel.getRates()
        }
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
            unitaryValueTextView.text =
                FormatUtils.BrazilianFormats.brCurrencyFormat.format(rate.unitaryRate)
            variationTextView.text = StringUtils.getVariationText(rate.variation)
            dateTimeTextView.text = getString(
                R.string.popup_date_time,
                FormatUtils.BrazilianFormats.brDateFormat.format(rate.rateDate),
                FormatUtils.BrazilianFormats.brTimeFormat.format(rate.rateDate)
            )

            show()
        }
    }

}