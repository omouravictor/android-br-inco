package com.omouravictor.ratesbr.presenter.stocks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesbr.data.local.entity.toStockUiModel
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgfinanceapi.stock.ApiStocksResponse
import com.omouravictor.ratesbr.data.network.hgfinanceapi.stock.toStocksEntityList
import com.omouravictor.ratesbr.data.network.hgfinanceapi.stock.toStocksUiModelList
import com.omouravictor.ratesbr.data.repository.StocksRepository
import com.omouravictor.ratesbr.presenter.base.DataSource
import com.omouravictor.ratesbr.presenter.base.UiResultStatus
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
import com.omouravictor.ratesbr.util.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StocksViewModel @ViewModelInject constructor(
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
            stocksRepository.getRemoteStocks(apiFields).collect { result ->
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