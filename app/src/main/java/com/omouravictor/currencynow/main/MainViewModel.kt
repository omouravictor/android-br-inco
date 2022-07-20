package com.omouravictor.currencynow.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.currencynow.data.models.Conversion
import com.omouravictor.currencynow.data.models.CurrencyApiResponse
import com.omouravictor.currencynow.database.entity.RatesEntity
import com.omouravictor.currencynow.util.DispatcherProvider
import com.omouravictor.currencynow.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

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

    private val toCurrencies = ("BRL,USD,EUR,JPY,GBP,CAD")
    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convertFromApi(selectedCurrency: Int, amountStr: String) {
        val amount = amountStr.toFloatOrNull()
        if (amount == null) {
            _conversion.value = CurrencyEvent.Empty
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading
            tryRatesFromApi(selectedCurrency, amount)
        }
    }

    private suspend fun tryRatesFromApi(selectedCurrency: Int, amount: Float) {
        val fromCurrency = getCurrencySymbol(selectedCurrency)
        when (val apiResponse = repository.getRatesFromApi(fromCurrency, toCurrencies)) {
            is Resource.Success -> {
                val rates = getRatesEntity(apiResponse.data!!, Date())
                val conversions = getConversionsForResult(fromCurrency, amount, rates)
                repository.insertRatesOnDb(rates)
                _conversion.value = CurrencyEvent.Success(conversions)
            }
            is Resource.Error -> {
                tryRatesFromDb(fromCurrency, amount)
            }
        }
    }

    fun convertFromDb(selectedCurrency: Int, amountStr: String) {
        val amount = amountStr.toFloatOrNull()
        if (amount == null) {
            _conversion.value = CurrencyEvent.Empty
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading
            val fromCurrency = getCurrencySymbol(selectedCurrency)
            tryRatesFromDb(fromCurrency, amount)
        }
    }

    private fun tryRatesFromDb(fromCurrency: String, amount: Float) {
        val rates = repository.getRatesFromDb(fromCurrency)
        if (rates != null) {
            val conversions = getConversionsForResult(fromCurrency, amount, rates)
            _conversion.value = CurrencyEvent.Success(conversions)
        } else {
            _conversion.value = CurrencyEvent.Failure("Não foi possível obter os dados.")
        }
    }

    private fun getRatesEntity(apiResponse: CurrencyApiResponse, ratesDate: Date): RatesEntity =
        RatesEntity(
            apiResponse.base,
            apiResponse.rates.uSD,
            apiResponse.rates.eUR,
            apiResponse.rates.jPY,
            apiResponse.rates.gBP,
            apiResponse.rates.cAD,
            apiResponse.rates.bRL,
            ratesDate
        )

    private fun getConversionsForResult(
        fromCurrency: String,
        amount: Float,
        rates: RatesEntity
    ): List<Conversion> {
        val list: MutableList<Conversion> = mutableListOf()

        arrayOf("BRL", "USD", "EUR", "JPY", "GBP", "CAD").forEach { toCurrency ->
            val rate = when (toCurrency) {
                "BRL" -> rates.bRL
                "USD" -> rates.uSD
                "EUR" -> rates.eUR
                "JPY" -> rates.jPY
                "GBP" -> rates.gBP
                "CAD" -> rates.cAD
                else -> 0.0
            }

            list.add(Conversion(fromCurrency, toCurrency, amount, rate, rates.date))
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
}