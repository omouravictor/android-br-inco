package com.omouravictor.ratesnow.presenter.rates

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.omouravictor.ratesnow.data.local.entity.CurrencyEntity
import com.omouravictor.ratesnow.data.network.base.NetworkResultStatus
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

    init {
        getRates()
    }

    fun getRates() {
        viewModelScope.launch {
            ratesApiRepository.getRemoteRates(toCurrencies).collect { networkResultStatus ->
                when (networkResultStatus) {
                    is NetworkResultStatus.Success -> {
                        val listCurrencyEntity =
                            networkResultStatus.data.toListCurrencyEntity(toCurrencies)
                        ratesLocalRepository.insertRates(listCurrencyEntity)
                        rates.postValue(ResultUiState.Success(listCurrencyEntity))
                    }
                    is NetworkResultStatus.Error -> {
                        ratesLocalRepository.getLocalRates().collect {
                            if (it.isNotEmpty()) {
                                rates.postValue(ResultUiState.Success(it))
                            } else {
                                rates.postValue(ResultUiState.Error(networkResultStatus.e))
                            }
                        }
                    }
                    is NetworkResultStatus.Loading -> {
                        rates.postValue(ResultUiState.Loading)
                    }
                }
            }
        }
    }
}