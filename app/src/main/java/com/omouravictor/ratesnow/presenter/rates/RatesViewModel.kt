package com.omouravictor.ratesnow.presenter.rates

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.omouravictor.ratesnow.data.local.entity.CurrencyEntity
import com.omouravictor.ratesnow.data.network.base.ResultStatus
import com.omouravictor.ratesnow.data.network.hgbrasil.rates.toListCurrencyEntity
import com.omouravictor.ratesnow.data.repository.RatesApiRepository
import com.omouravictor.ratesnow.data.repository.RatesLocalRepository
import com.omouravictor.ratesnow.presenter.base.ResultUiState
import kotlinx.coroutines.launch

class RatesViewModel @ViewModelInject constructor(
    private val ratesLocalRepository: RatesLocalRepository,
    private val ratesApiRepository: RatesApiRepository
) : ViewModel() {
    val rates = MutableLiveData<ResultUiState<List<CurrencyEntity>>>()
    private val toCurrencies = "USD,EUR,GBP,CAD,AUD,JPY"

    fun getRates(fromCurrency: String) {
        viewModelScope.launch {
            ratesApiRepository.getRemoteRates(toCurrencies).collect { resultStatus ->
                when (resultStatus) {
                    is ResultStatus.Success -> {
                        val listCurrencyEntity =
                            resultStatus.data.toListCurrencyEntity(toCurrencies)
                        ratesLocalRepository.insertRates(listCurrencyEntity)
                        rates.postValue(ResultUiState.Success(listCurrencyEntity))
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
    }
}