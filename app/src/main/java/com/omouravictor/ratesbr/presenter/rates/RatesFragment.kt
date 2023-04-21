package com.omouravictor.ratesbr.presenter.rates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.ratesbr.databinding.FragmentRatesBinding
import com.omouravictor.ratesbr.presenter.base.UiResultState
import com.omouravictor.ratesbr.presenter.converter.ConverterViewModel
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel

class RatesFragment : Fragment() {

    private lateinit var ratesBinding: FragmentRatesBinding
    private val ratesViewModel: RatesViewModel by activityViewModels()
    private val converterViewModel: ConverterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ratesBinding = FragmentRatesBinding.inflate(layoutInflater, container, false)
        return ratesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSwipeRefreshLayout()

        ratesViewModel.ratesResult.observe(viewLifecycleOwner) {
            when (it) {
                is UiResultState.Success -> {
                    configureRecyclerView(it.data)
                    ratesBinding.swipeRefreshLayout.isRefreshing = false
                    ratesBinding.progressBar.isVisible = false
                    ratesBinding.recyclerViewRates.isVisible = true
                    ratesBinding.includeViewError.root.isVisible = false
                }
                is UiResultState.Error -> {
                    ratesBinding.swipeRefreshLayout.isRefreshing = false
                    ratesBinding.progressBar.isVisible = false
                    ratesBinding.recyclerViewRates.isVisible = false
                    ratesBinding.includeViewError.root.isVisible = true
                    ratesBinding.includeViewError.textViewErrorMessage.text = it.e.message
                }
                is UiResultState.Loading -> {
                    ratesBinding.swipeRefreshLayout.isRefreshing = false
                    ratesBinding.progressBar.isVisible = true
                    ratesBinding.recyclerViewRates.isVisible = false
                    ratesBinding.includeViewError.root.isVisible = false
                }
            }
        }
    }

    private fun initSwipeRefreshLayout() {
        ratesBinding.swipeRefreshLayout.setOnRefreshListener {
            ratesViewModel.getRates()
        }
    }

    private fun configureRecyclerView(ratesList: List<RateUiModel>) {
        ratesBinding.recyclerViewRates.apply {
            adapter = RatesAdapter(ratesList) { ratesOnClickItem(it) }
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun ratesOnClickItem(rateUiModel: RateUiModel) {
        converterViewModel.setInitialRateAndConversionResults(rateUiModel)
        val action = RatesFragmentDirections.actionRatesFragmentToConverterFragment()
        findNavController().navigate(action)
    }

}