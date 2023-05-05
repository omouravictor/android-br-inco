package com.omouravictor.ratesbr.presenter.rates

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.ratesbr.R
import com.omouravictor.ratesbr.databinding.FragmentRatesBinding
import com.omouravictor.ratesbr.presenter.base.UiResultStatus
import com.omouravictor.ratesbr.presenter.converter.ConverterViewModel
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel

class RatesFragment : Fragment() {

    private lateinit var bottomSheetDialog: Dialog
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

        initBottomSheetDialog()
        initTryAgainButton()

        ratesViewModel.ratesResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiResultStatus.Success -> {
                    configureRecyclerView(result.data)
                    binding.progressBar.isVisible = false
                    binding.recyclerViewRates.isVisible = true
                    binding.includeViewError.root.isVisible = false
                }
                is UiResultStatus.Error -> {
                    binding.progressBar.isVisible = false
                    binding.recyclerViewRates.isVisible = false
                    binding.includeViewError.root.isVisible = true
                    binding.includeViewError.textViewErrorMessage.text = result.e.message
                }
                is UiResultStatus.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.recyclerViewRates.isVisible = false
                    binding.includeViewError.root.isVisible = false
                }
            }
        }
    }

    private fun initBottomSheetDialog() {
        bottomSheetDialog = Dialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_rates_dialog)
        bottomSheetDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        bottomSheetDialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun initTryAgainButton() {
        binding.includeViewError.buttonTryAgain.setOnClickListener {
            ratesViewModel.getRates()
        }
    }

    private fun configureRecyclerView(ratesList: List<RateUiModel>) {
        binding.recyclerViewRates.apply {
            adapter = RatesAdapter(ratesList) { showBottomSheetDialog(it) }
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showBottomSheetDialog(rateUiModel: RateUiModel) {
        with(bottomSheetDialog) {
            val converterLayout = findViewById<LinearLayout>(R.id.converterLayout)
            val detailsLayout = findViewById<LinearLayout>(R.id.detailsLayout)

            converterLayout.setOnClickListener {
                bottomSheetDialog.dismiss()
                prepareAndNavigateToConverterFragment(rateUiModel)
            }

            detailsLayout.setOnClickListener {
                bottomSheetDialog.dismiss()
            }

            show()
        }
    }

    private fun prepareAndNavigateToConverterFragment(rateUiModel: RateUiModel) {
        converterViewModel.setInitialValues(rateUiModel)
        val action = RatesFragmentDirections.actionRatesFragmentToConverterFragment()
        findNavController().navigate(action)
    }

}