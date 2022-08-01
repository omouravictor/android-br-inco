package com.omouravictor.ratesnow.ui.rate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.ratesnow.adapter.RateAdapter
import com.omouravictor.ratesnow.databinding.FragmentRateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateFragment : Fragment() {

    private lateinit var binding: FragmentRateBinding
    private val viewModel: RateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initEtAmount()
        initSpFromCurrency()
        initSwipeRefreshLayout()
        initConversionsRecyclerView()

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event ->
                when (event) {
                    is RateViewModel.ConversionEvent.Success -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.progressBar.isVisible = false
                        binding.rvConversions.isVisible = true
                    }
                    is RateViewModel.ConversionEvent.Failure -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.progressBar.isVisible = false
                        binding.rvConversions.isVisible = true
                        Toast.makeText(context, event.errorText, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is RateViewModel.ConversionEvent.Loading -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.progressBar.isVisible = true
                        binding.rvConversions.isVisible = false
                    }
                    is RateViewModel.ConversionEvent.Empty -> {
                        binding.rvConversions.isVisible = false
                    }
                }
            }
        }
    }

    private fun initEtAmount() {
        binding.etAmount.setText("1")
        binding.etAmount.setSelection(1)
        binding.etAmount.doAfterTextChanged { text ->
            viewModel.convert(text.toString())
        }
    }

    private fun initSpFromCurrency() {
        binding.spFromCurrency.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                viewModel.getRates(position, binding.etAmount.text.toString())
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getRates(
                binding.spFromCurrency.selectedItemPosition,
                binding.etAmount.text.toString()
            )
        }
    }

    private fun initConversionsRecyclerView() {
        viewModel.conversionList.observe(this, Observer {
            binding.rvConversions.apply {
                adapter = RateAdapter(it)
                layoutManager = LinearLayoutManager(context)
            }
        })
    }
}