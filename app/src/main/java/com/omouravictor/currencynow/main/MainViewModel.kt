package com.omouravictor.currencynow.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.currencynow.data.models.Conversion
import com.omouravictor.currencynow.data.models.Rates
import com.omouravictor.currencynow.util.DispatcherProvider
import com.omouravictor.currencynow.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class CurrencyEvent {
        class Success(val conversionsList: List<Conversion>) : CurrencyEvent()
        class Failure(val errorText: String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(
        amountStr: String,
        selectedCurrency: Int
    ) {
        val amount = amountStr.toFloatOrNull()
        if (amount == null) {
            _conversion.value = CurrencyEvent.Empty
            return
        }

        val fromCurrency = getCurrencySymbol(selectedCurrency)
        val toCurrencies = ("BRL,USD,EUR,JPY,GBP,CAD,").replaceFirst("$fromCurrency,", "")

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading
            when (val apiResponse = repository.getRates(fromCurrency, toCurrencies)) {
                is Resource.Error -> _conversion.value =
                    CurrencyEvent.Failure(apiResponse.message!!)
                is Resource.Success -> {
                    val responseData = apiResponse.data!!
                    val conversions = getConversionsForResult(
                        fromCurrency,
                        toCurrencies,
                        amount,
                        responseData.rates
                    )
                    _conversion.value = CurrencyEvent.Success(conversions)
                }
            }
        }
    }

    private fun getConversionsForResult(
        fromCurrency: String,
        toCurrencies: String,
        amount: Float,
        rateList: Rates
    ): List<Conversion> {
        val list: MutableList<Conversion> = mutableListOf()
        val toCurrencyArray = toCurrencies.split(",")

        for (i in 0 until toCurrencyArray.lastIndex) {
            val toCurrency = toCurrencyArray[i]
            val rate = getRateForCurrency(toCurrency, rateList)
            list.add(Conversion(fromCurrency, toCurrency, amount, rate))
        }

        return list
    }

    private fun getCurrencySymbol(selectedCurrency: Int) = when (selectedCurrency) {
        0 -> "USD"
        1 -> "EUR"
        2 -> "JPY"
        3 -> "GBP"
        4 -> "CAD"
        5 -> "BRL"
        else -> ""
    }

    private fun getRateForCurrency(currency: String, rates: Rates) =
        when (currency) {
            "BRL" -> rates.bRL
            "USD" -> rates.uSD
            "EUR" -> rates.eUR
            "JPY" -> rates.jPY
            "GBP" -> rates.gBP
            "CAD" -> rates.cAD
            else -> -1.0
        }
}