package com.omouravictor.ratesnow.presenter.rates

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesnow.data.local.entity.CurrencyEntity
import com.omouravictor.ratesnow.data.network.base.ResultStatus
import com.omouravictor.ratesnow.data.network.hgbrasil.rates.toListCurrencyEntity
import com.omouravictor.ratesnow.data.repository.RatesApiRepository
import com.omouravictor.ratesnow.data.repository.RatesLocalRepository
import com.omouravictor.ratesnow.presenter.base.ResultUiState
import kotlinx.coroutines.launch
import java.lang.Exception

class RatesViewModel @ViewModelInject constructor(
    private val ratesLocalRepository: RatesLocalRepository,
    private val ratesApiRepository: RatesApiRepository
) : ViewModel() {
    val rates = MutableLiveData<ResultUiState<List<CurrencyEntity>>>()
    private val toCurrencies = "USD,EUR,GBP,CAD,AUD,JPY"

    fun getRates(fromCurrency: String) {
        viewModelScope.launch {
            val resultStatus = try {
                ratesApiRepository.getRemoteRates(toCurrencies)
            } catch (e: Exception) {
                ResultStatus.Error(e)
            }

            when (resultStatus) {
                is ResultStatus.Success -> {
                    resultStatus.data?.let {
                        val listCurrencyEntity = it.toListCurrencyEntity(toCurrencies)
                        saveRatesLocally(listCurrencyEntity)
                        rates.postValue(ResultUiState.Success(listCurrencyEntity))
                    }
                }
                is ResultStatus.Error -> {
                    ratesLocalRepository.getLocalRates().collect {
                        if (it.isNotEmpty()) {
                            rates.postValue(ResultUiState.Success(it))
                        } else {
                            rates.postValue(ResultUiState.Error(resultStatus.e))
                        }
                    }
                }
                is ResultStatus.Loading -> {
                    rates.postValue(ResultUiState.Loading)
                }
            }
        }
    }

    private fun saveRatesLocally(currencyEntity: List<CurrencyEntity>) {
        viewModelScope.launch {
            ratesLocalRepository.insertRates(currencyEntity)
        }
    }
}