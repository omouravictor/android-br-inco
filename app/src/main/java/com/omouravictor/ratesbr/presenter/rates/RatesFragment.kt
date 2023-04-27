package com.omouravictor.ratesbr.presenter.rates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.ratesbr.R
import com.omouravictor.ratesbr.databinding.FragmentRatesBinding
import com.omouravictor.ratesbr.presenter.base.UiResultState
import com.omouravictor.ratesbr.presenter.converter.ConverterViewModel
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel

class RatesFragment : Fragment() {

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

        initSwipeRefreshLayout()

        ratesViewModel.ratesResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiResultState.Success -> {
                    configureRecyclerView(result.data)
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.recyclerViewRates.isVisible = true
                    binding.includeViewError.root.isVisible = false
                }
                is UiResultState.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.recyclerViewRates.isVisible = false
                    binding.includeViewError.root.isVisible = true
                    binding.includeViewError.textViewErrorMessage.text = result.e.message
                }
                is UiResultState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                    binding.recyclerViewRates.isVisible = false
                    binding.includeViewError.root.isVisible = false
                }
            }
        }
    }

    private fun initSwipeRefreshLayout() {
        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)
        binding.swipeRefreshLayout.setColorSchemeColors(greenColor, greenColor, greenColor)
        binding.swipeRefreshLayout.setOnRefreshListener { ratesViewModel.getRates() }
    }

    private fun configureRecyclerView(ratesList: List<RateUiModel>) {
        binding.recyclerViewRates.apply {
            adapter = RatesAdapter(ratesList) { ratesAdapterOnClickItem(it) }
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun ratesAdapterOnClickItem(rateUiModel: RateUiModel) {
        converterViewModel.setInitialValues(rateUiModel)
        val action = RatesFragmentDirections.actionRatesFragmentToConverterFragment()
        findNavController().navigate(action)
    }

}