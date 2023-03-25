package com.omouravictor.ratesbr.presenter.rates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.ratesbr.databinding.FragmentRatesBinding
import com.omouravictor.ratesbr.presenter.base.UiResultState
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatesFragment : Fragment() {

    private lateinit var ratesBinding: FragmentRatesBinding
    private val ratesViewModel: RatesViewModel by activityViewModels()
    private var ratesAdapter = RatesAdapter(emptyList())

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

        initEtAmount()
        initSwipeRefreshLayout()

        ratesViewModel.rates.observe(viewLifecycleOwner) {
            when (it) {
                is UiResultState.Success -> {
                    initAdapter(it.data)
                    ratesBinding.swipeRefreshLayout.isRefreshing = false
                    ratesBinding.progressBar.isVisible = false
                    ratesBinding.rvConversions.isVisible = true
                }
                is UiResultState.Error -> {
                    ratesBinding.swipeRefreshLayout.isRefreshing = false
                    ratesBinding.progressBar.isVisible = false
                    ratesBinding.rvConversions.isVisible = false
                    Toast.makeText(context, it.e.message, Toast.LENGTH_SHORT).show()
                }
                is UiResultState.Loading -> {
                    ratesBinding.swipeRefreshLayout.isRefreshing = false
                    ratesBinding.progressBar.isVisible = true
                    ratesBinding.rvConversions.isVisible = false
                }
            }
        }
    }

    private fun initEtAmount() {
        ratesBinding.etAmount.setText("1")
        ratesBinding.etAmount.setSelection(1)
        ratesBinding.etAmount.doAfterTextChanged {
            val amount = it.toString()
            if (amount.isNotEmpty())
                ratesAdapter.updateConversionRates(amount.toDouble())
            else
                ratesAdapter.updateConversionRates(0.0)
        }
    }

    private fun initSwipeRefreshLayout() {
        ratesBinding.swipeRefreshLayout.setOnRefreshListener {
            ratesViewModel.getRates()
        }
    }

    private fun initAdapter(ratesList: List<RateUiModel>) {
        ratesAdapter = RatesAdapter(ratesList)

        ratesBinding.rvConversions.apply {
            adapter = ratesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

}