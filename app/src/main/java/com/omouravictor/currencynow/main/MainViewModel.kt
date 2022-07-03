package com.omouravictor.currencynow.main

import android.content.Context
import android.net.ConnectivityManager
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.currencynow.data.models.Rates
import com.omouravictor.currencynow.util.DispatcherProvider
import com.omouravictor.currencynow.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.round

class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class CurrencyEvent {
        class Success(val resultText: String) : CurrencyEvent()
        class Failure(val errorText: String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(
        context: Context,
        amountStr: String,
        fromCurrencyName: String,
        toCurrencyName: String
    ) {
        if (!isOnline(context)) {
            _conversion.value = CurrencyEvent.Failure("Sem conexão :(")
            return
        }

        val fromAmount = amountStr.toFloatOrNull()
        if (fromAmount == null) {
            _conversion.value = CurrencyEvent.Failure("Valor inválido")
            return
        }

        val fromCurrency = getCurrencyAbbreviation(fromCurrencyName)
        val toCurrency = getCurrencyAbbreviation(toCurrencyName)

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading
            when (val ratesResponse = repository.getRates(fromCurrency)) {
                is Resource.Error -> _conversion.value =
                    CurrencyEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.rates
                    val rate = getRateForCurrency(toCurrency, rates)
                    if (rate == null) {
                        _conversion.value = CurrencyEvent.Failure("Erro inesperado")
                    } else {
                        val decFormat = DecimalFormat("#,###.00")
                        val amount = decFormat.format(fromAmount)
                        val result = decFormat.format(round(fromAmount * rate * 100) / 100)
                        _conversion.value = CurrencyEvent.Success(
                            "$amount $fromCurrency = $result $toCurrency"
                        )
                    }
                }
            }
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) != null
    }

    private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
        "USD" -> rates.uSD
        "EUR" -> rates.eUR
        "JPY" -> rates.jPY
        "GBP" -> rates.gBP
        "CAD" -> rates.cAD
        "BRL" -> rates.bRL
        else -> null
    }

    private fun getCurrencyAbbreviation(currencyName: String): String = when (currencyName) {
        "Dólar (USD)" -> "USD"
        "Euro (EUR)" -> "EUR"
        "Iene (JPY)" -> "JPY"
        "Libra (GBP)" -> "GBP"
        "Dólar (CAD)" -> "CAD"
        "Real (BRL)" -> "BRL"
        else -> ""
    }

}