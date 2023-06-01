package com.omouravictor.ratesbr.presenter.rates

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesbr.data.local.entity.toRateUiModel
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgfinanceapi.rates.toListRateEntity
import com.omouravictor.ratesbr.data.repository.RatesRepository
import com.omouravictor.ratesbr.presenter.base.DataSource
import com.omouravictor.ratesbr.presenter.base.UiResultStatus
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RatesViewModel @ViewModelInject constructor(
    private val ratesRepository: RatesRepository
) : ViewModel() {

    val ratesResult = MutableLiveData<UiResultStatus<Pair<List<RateUiModel>, DataSource>>>()
    private val currencies = "USD,EUR,JPY,GBP,CAD,AUD,ARS,CNY"

    init {
        getRates()
    }

    fun getRates() {
        viewModelScope.launch(Dispatchers.IO) {
            ratesRepository.getRemoteRates(currencies).collect { networkResultStatus ->
                when (networkResultStatus) {
                    is NetworkResultStatus.Success -> {
                        val remoteRatesList = networkResultStatus.data.toListRateEntity()
                        val ratesUiModelList = remoteRatesList.map { it.toRateUiModel() }
                        ratesRepository.insertRates(remoteRatesList)
                        postUiResultStatusSuccess(ratesUiModelList, DataSource.NETWORK)
                    }

                    is NetworkResultStatus.Error -> {
                        val localRatesList = ratesRepository.getLocalRates()
                        if (localRatesList.isNotEmpty()) {
                            val ratesUiModelList = localRatesList.map { it.toRateUiModel() }
                            postUiResultStatusSuccess(ratesUiModelList, DataSource.LOCAL)
                        } else {
                            ratesResult.postValue(UiResultStatus.Error(networkResultStatus.e))
                        }
                    }

                    is NetworkResultStatus.Loading -> {
                        ratesResult.postValue(UiResultStatus.Loading)
                    }
                }
            }
        }
    }

    private fun postUiResultStatusSuccess(
        ratesUiModelList: List<RateUiModel>,
        dataSource: DataSource
    ) {
        ratesResult.postValue(
            UiResultStatus.Success(
                Pair(ratesUiModelList, dataSource)
            )
        )
    }
}