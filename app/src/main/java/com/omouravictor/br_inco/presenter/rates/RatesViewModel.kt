package com.omouravictor.br_inco.presenter.rates

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.br_inco.data.local.entity.toRateUiModel
import com.omouravictor.br_inco.data.network.base.NetworkResultStatus
import com.omouravictor.br_inco.data.network.hgfinanceapi.rates.ApiRatesResponse
import com.omouravictor.br_inco.data.network.hgfinanceapi.rates.toRatesEntityList
import com.omouravictor.br_inco.data.network.hgfinanceapi.rates.toRatesUiModelList
import com.omouravictor.br_inco.data.repository.RatesRepository
import com.omouravictor.br_inco.presenter.base.DataSource
import com.omouravictor.br_inco.presenter.base.UiResultStatus
import com.omouravictor.br_inco.presenter.rates.model.RateUiModel
import com.omouravictor.br_inco.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RatesViewModel @Inject constructor(
    private val ratesRepository: RatesRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    val ratesResult = MutableLiveData<UiResultStatus<Pair<List<RateUiModel>, DataSource>>>()
    private val apiFields = "USD,EUR,JPY,GBP,CAD,AUD,ARS,CNY"

    init {
        getRates()
    }

    fun getRates() {
        viewModelScope.launch(dispatchers.io) {
            ratesRepository.getRemoteRates(apiFields).collectLatest { result ->
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