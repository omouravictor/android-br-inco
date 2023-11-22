package com.omouravictor.br_inco.presenter.stocks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.br_inco.data.local.entity.toStockUiModel
import com.omouravictor.br_inco.data.network.base.NetworkResultStatus
import com.omouravictor.br_inco.data.network.hgfinanceapi.stock.ApiStocksResponse
import com.omouravictor.br_inco.data.network.hgfinanceapi.stock.toStocksEntityList
import com.omouravictor.br_inco.data.network.hgfinanceapi.stock.toStocksUiModelList
import com.omouravictor.br_inco.data.repository.StocksRepository
import com.omouravictor.br_inco.presenter.base.DataSource
import com.omouravictor.br_inco.presenter.base.UiResultStatus
import com.omouravictor.br_inco.presenter.stocks.model.StockUiModel
import com.omouravictor.br_inco.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StocksViewModel @Inject constructor(
    private val stocksRepository: StocksRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    val stocksResult = MutableLiveData<UiResultStatus<Pair<List<StockUiModel>, DataSource>>>()
    private val apiFields = "stocks"

    init {
        getStocks()
    }

    fun getStocks() {
        viewModelScope.launch(dispatchers.io) {
            stocksRepository.getRemoteStocks(apiFields).collectLatest { result ->
                when (result) {
                    is NetworkResultStatus.Success -> handleNetworkSuccessResult(result.data)
                    is NetworkResultStatus.Error -> handleNetworkErrorResult(result.message)
                    is NetworkResultStatus.Loading -> handleNetworkLoadingResult()
                }
            }
        }
    }

    private fun postUiResultStatusSuccess(
        stocksUiModelList: List<StockUiModel>,
        dataSource: DataSource
    ) {
        stocksResult.postValue(UiResultStatus.Success(Pair(stocksUiModelList, dataSource)))
    }

    private suspend fun handleNetworkSuccessResult(apiStocksResponse: ApiStocksResponse) {
        stocksRepository.insertStocks(apiStocksResponse.toStocksEntityList())
        postUiResultStatusSuccess(apiStocksResponse.toStocksUiModelList(), DataSource.NETWORK)
    }

    private fun handleNetworkErrorResult(message: String) {
        val localStocksEntityList = stocksRepository.getLocalStocks()
        if (localStocksEntityList.isNotEmpty()) {
            val localStocksUiModelList = localStocksEntityList.map { it.toStockUiModel() }
            postUiResultStatusSuccess(localStocksUiModelList, DataSource.LOCAL)
        } else {
            stocksResult.postValue(UiResultStatus.Error(message))
        }
    }

    private fun handleNetworkLoadingResult() {
        stocksResult.postValue(UiResultStatus.Loading)
    }

}