package com.omouravictor.ratesbr.presenter.rates

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesbr.data.local.entity.toRateUiModel
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgfinanceapi.rates.ApiRatesResponse
import com.omouravictor.ratesbr.data.network.hgfinanceapi.rates.toRatesEntityList
import com.omouravictor.ratesbr.data.network.hgfinanceapi.rates.toRatesUiModelList
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
    private val apiFields = "USD,EUR,JPY,GBP,CAD,AUD,ARS,CNY"

    init {
        getRates()
    }

    fun getRates() {
        viewModelScope.launch(Dispatchers.IO) {
            ratesRepository.getRemoteRates(apiFields).collect { result ->
                when (result) {
                    is NetworkResultStatus.Success -> handleNetworkSuccessResult(result.data)
                    is NetworkResultStatus.Error -> handleNetworkErrorResult(result.message)
                    is NetworkResultStatus.Loading -> handleNetworkLoadingResult()
                }
            }
        }
    }

    private fun postUiResultStatusSuccess(
        ratesUiModelList: List<RateUiModel>,
        dataSource: DataSource
    ) {
        ratesResult.postValue(UiResultStatus.Success(Pair(ratesUiModelList, dataSource)))
    }

    private suspend fun handleNetworkSuccessResult(apiRatesResponse: ApiRatesResponse) {
        ratesRepository.insertRates(apiRatesResponse.toRatesEntityList())
        postUiResultStatusSuccess(apiRatesResponse.toRatesUiModelList(), DataSource.NETWORK)
    }

    private fun handleNetworkErrorResult(message: String) {
        val localRatesEntityList = ratesRepository.getLocalRates()
        if (localRatesEntityList.isNotEmpty()) {
            val localRatesUiModelList = localRatesEntityList.map { it.toRateUiModel() }
            postUiResultStatusSuccess(localRatesUiModelList, DataSource.LOCAL)
        } else {
            ratesResult.postValue(UiResultStatus.Error(message))
        }
    }

    private fun handleNetworkLoadingResult() {
        ratesResult.postValue(UiResultStatus.Loading)
    }

}