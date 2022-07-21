package com.omouravictor.ratesnow.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesnow.data.models.ApiResponse
import com.omouravictor.ratesnow.data.models.Conversion
import com.omouravictor.ratesnow.database.entity.RatesEntity
import com.omouravictor.ratesnow.util.DispatcherProvider
import com.omouravictor.ratesnow.util.Resource
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
            val fromCurrency = getCurrencySymbol(selectedCurrency)
            tryRatesFromApi(fromCurrency, amount)
        }
    }

    private suspend fun tryRatesFromApi(fromCurrency: String, amount: Float) {
        when (val ratesApiRequest = repository.getRatesFromApi(fromCurrency, toCurrencies)) {
            is Resource.Success -> {
                val rates = getRatesEntity(ratesApiRequest.data!!, Date())
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
            _conversion.value = CurrencyEvent.Failure(
                "Não foi possível obter os dados.\nVerifique sua conexão :("
            )
        }
    }

    private fun getRatesEntity(apiResponse: ApiResponse, ratesDate: Date): RatesEntity =
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

        arrayOf("BRL", "USD", "EUR", "JPY", "GBP", "CAD").forEach {
            if (it != fromCurrency) {
                val rate = when (it) {
                    "BRL" -> rates.bRL
                    "USD" -> rates.uSD
                    "EUR" -> rates.eUR
                    "JPY" -> rates.jPY
                    "GBP" -> rates.gBP
                    "CAD" -> rates.cAD
                    else -> 0.0
                }
                list.add(Conversion(fromCurrency, it, amount, rate, rates.date))
            }
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