package com.omouravictor.currencynow

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.omouravictor.currencynow.adapter.CurrenciesAdapter
import com.omouravictor.currencynow.databinding.ActivityMainBinding
import com.omouravictor.currencynow.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initCurrenciesRecyclerView()
        initCurrencies()

        binding.etAmount.doAfterTextChanged { text ->
            val amountStr = text.toString()
            if (amountStr.isNotEmpty()) {
                viewModel.convert(
                    this, amountStr, binding.spFromCurrency.selectedItemPosition
                )
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event ->
                when (event) {
                    is MainViewModel.CurrencyEvent.Success -> {
                        binding.progressBar.isVisible = false
                        binding.rvCurrencies.isVisible = true
                        (binding.rvCurrencies.adapter as CurrenciesAdapter).setList(event.resultRatesList)
                    }
                    is MainViewModel.CurrencyEvent.Failure -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(applicationContext, event.errorText, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is MainViewModel.CurrencyEvent.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.rvCurrencies.isVisible = false
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun initCurrencies () {
        binding.etAmount.setText("1")
        viewModel.convert(this, binding.etAmount.text.toString(), 0)
    }

    private fun initCurrenciesRecyclerView() {
        binding.rvCurrencies.apply {
            adapter = CurrenciesAdapter(mutableListOf(), Locale("pt", "BR"))
            layoutManager = LinearLayoutManager(context)
        }
    }

}