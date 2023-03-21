package com.omouravictor.ratesnow.presenter.rates

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesnow.data.local.entity.toRateUiModel
import com.omouravictor.ratesnow.data.network.base.NetworkResultStatus
import com.omouravictor.ratesnow.data.network.hgbrasil.rates.toListRateEntity
import com.omouravictor.ratesnow.data.repository.RatesApiRepository
import com.omouravictor.ratesnow.data.repository.RatesLocalRepository
import com.omouravictor.ratesnow.presenter.base.UiResultState
import com.omouravictor.ratesnow.presenter.rates.model.RateUiModel
import kotlinx.coroutines.launch

class RatesViewModel @ViewModelInject constructor(
    private val ratesLocalRepository: RatesLocalRepository,
    private val ratesApiRepository: RatesApiRepository
) : ViewModel() {
    val rates = MutableLiveData<UiResultState<List<RateUiModel>>>()
    private val currencies = "USD,EUR,GBP,CAD,AUD,JPY"

    init {
        getRates()
    }

    fun getRates() {
        viewModelScope.launch {
            ratesApiRepository.getRemoteRates(currencies).collect { networkResultStatus ->
                when (networkResultStatus) {
                    is NetworkResultStatus.Success -> {
                        val listRateEntity = networkResultStatus.data.toListRateEntity(currencies)
                        ratesLocalRepository.insertRates(listRateEntity)
                        rates.postValue(UiResultState.Success(listRateEntity.map { it.toRateUiModel() }))
                    }

                    is NetworkResultStatus.Error -> {
                        ratesLocalRepository.getLocalRates().collect { localRates ->
                            if (localRates.isNotEmpty())
                                rates.postValue(UiResultState.Success(localRates.map { it.toRateUiModel() }))
                            else
                                rates.postValue(UiResultState.Error(networkResultStatus.e))
                        }
                    }

                    is NetworkResultStatus.Loading -> {
                        rates.postValue(UiResultState.Loading)
                    }
                }
            }
        }
    }
}