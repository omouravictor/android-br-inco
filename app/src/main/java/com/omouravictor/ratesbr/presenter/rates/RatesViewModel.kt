package com.omouravictor.ratesbr.presenter.rates

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesbr.data.local.entity.toRateUiModel
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgbrasil.rates.toListRateEntity
import com.omouravictor.ratesbr.data.repository.RatesApiRepository
import com.omouravictor.ratesbr.data.repository.RatesLocalRepository
import com.omouravictor.ratesbr.presenter.base.UiResultState
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import kotlinx.coroutines.launch

class RatesViewModel @ViewModelInject constructor(
    private val ratesLocalRepository: RatesLocalRepository,
    private val ratesApiRepository: RatesApiRepository
) : ViewModel() {
    val rates = MutableLiveData<UiResultState<List<RateUiModel>>>()
    private val currencies = "USD,EUR,JPY,GBP,CAD,AUD,ARS,CNY"

    init {
        getRates()
    }

    fun getRates() {
        viewModelScope.launch {
            ratesApiRepository.getRemoteRates(currencies).collect { networkResultStatus ->
                when (networkResultStatus) {
                    is NetworkResultStatus.Success -> {
                        val remoteRates = networkResultStatus.data.toListRateEntity()
                        ratesLocalRepository.insertRates(remoteRates)
                        rates.postValue(UiResultState.Success(remoteRates.map { it.toRateUiModel() }))
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