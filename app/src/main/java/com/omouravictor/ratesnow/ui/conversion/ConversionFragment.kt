package com.omouravictor.ratesnow.ui.conversion

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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.ratesnow.adapter.ConversionAdapter
import com.omouravictor.ratesnow.databinding.FragmentConversionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConversionFragment : Fragment() {

    private lateinit var binding: FragmentConversionsBinding
    private val viewModel: ConversionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConversionsBinding.inflate(layoutInflater, container, false)
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
                    is ConversionViewModel.CurrencyEvent.Success -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.progressBar.isVisible = false
                        binding.rvConversions.isVisible = true
                        (binding.rvConversions.adapter as ConversionAdapter).setList(event.conversionsList)
                    }
                    is ConversionViewModel.CurrencyEvent.Failure -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.progressBar.isVisible = false
                        binding.rvConversions.isVisible = false
                        Toast.makeText(context, event.errorText, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is ConversionViewModel.CurrencyEvent.Loading -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.progressBar.isVisible = true
                        binding.rvConversions.isVisible = false
                    }
                    is ConversionViewModel.CurrencyEvent.Empty -> {
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
            viewModel.convertFromDb(
                binding.spFromCurrency.selectedItemPosition,
                text.toString()
            )
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
                viewModel.convertFromApi(position, binding.etAmount.text.toString())
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.convertFromApi(
                binding.spFromCurrency.selectedItemPosition,
                binding.etAmount.text.toString()
            )
        }
    }

    private fun initConversionsRecyclerView() {
        binding.rvConversions.apply {
            adapter = ConversionAdapter(mutableListOf())
            layoutManager = LinearLayoutManager(context)
        }
    }

}