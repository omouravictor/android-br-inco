package com.omouravictor.currencynow.main

import android.content.Context
import android.net.ConnectivityManager
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
import kotlin.math.round

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
        context: Context,
        amountStr: String,
        selectedCurrency: Int
    ) {
        if (!isOnline(context)) {
            _conversion.value = CurrencyEvent.Failure("Sem conexão :(")
            return
        }

        val amount = amountStr.toFloatOrNull()
        if (amount == null) {
            _conversion.value = CurrencyEvent.Failure("Valor inválido")
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
        rates: Rates
    ): List<Conversion> {
        val conversions: MutableList<Conversion> = mutableListOf()
        val toCurrencyArray = toCurrencies.split(",")

        for (i in 0 until toCurrencyArray.lastIndex) {
            val toCurrency = toCurrencyArray[i]
            val rateValue = getRateValueForCurrency(toCurrency, rates)
            conversions.add(Conversion(fromCurrency, toCurrency, round(amount * rateValue!! * 100) / 100))
        }

        return conversions
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

    private fun getRateValueForCurrency(currency: String, rates: Rates) =
        when (currency) {
            "BRL" -> rates.bRL
            "USD" -> rates.uSD
            "EUR" -> rates.eUR
            "JPY" -> rates.jPY
            "GBP" -> rates.gBP
            "CAD" -> rates.cAD
            else -> null
        }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) != null
    }

}