package com.omouravictor.ratesnow.presenter.rates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.ratesnow.databinding.FragmentRatesBinding
import com.omouravictor.ratesnow.presenter.base.ResultUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatesFragment : Fragment() {

    private lateinit var ratesBinding: FragmentRatesBinding
    private val ratesViewModel: RatesViewModel by activityViewModels()
    private lateinit var ratesAdapter: RatesAdapter

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

        initAdapter()
        initEtAmount()
        initSpinner()
        initSwipeRefreshLayout()

        ratesViewModel.rates.observe(viewLifecycleOwner) {
            when (it) {
                is ResultUiState.Success -> {
                    ratesAdapter.submitList(it.data)
                    ratesBinding.swipeRefreshLayout.isRefreshing = false
                    ratesBinding.progressBar.isVisible = false
                    ratesBinding.rvConversions.isVisible = true
                }
                is ResultUiState.Error -> {
                    ratesBinding.swipeRefreshLayout.isRefreshing = false
                    ratesBinding.progressBar.isVisible = false
                    ratesBinding.rvConversions.isVisible = false
                    Toast.makeText(context, it.e.message, Toast.LENGTH_SHORT).show()
                }
                is ResultUiState.Loading -> {
                    ratesBinding.swipeRefreshLayout.isRefreshing = false
                    ratesBinding.progressBar.isVisible = true
                    ratesBinding.rvConversions.isVisible = false
                }
            }
        }
    }

    private fun initAdapter() {
        ratesAdapter = RatesAdapter()

        ratesBinding.rvConversions.apply {
            adapter = ratesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initEtAmount() {
        ratesBinding.etAmount.setText("1")
        ratesBinding.etAmount.setSelection(1)
        ratesBinding.etAmount.doAfterTextChanged { text ->
//            ratesViewModel.convert(text.toString())
        }
    }

    private fun initSpinner() {
        ratesBinding.spFromCurrency.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                ratesViewModel.getRates(
                    ratesBinding.spFromCurrency.selectedItem.toString(),
//                    ratesBinding.etAmount.text.toString()
                )
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
    }

    private fun initSwipeRefreshLayout() {
        ratesBinding.swipeRefreshLayout.setOnRefreshListener {
            ratesViewModel.getRates(
                ratesBinding.spFromCurrency.selectedItem.toString(),
//                ratesBinding.etAmount.text.toString()
            )
        }
    }

}