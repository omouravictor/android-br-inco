package com.omouravictor.ratesbr.presenter.stocks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesbr.data.local.entity.StockEntity
import com.omouravictor.ratesbr.data.local.entity.toStockUiModel
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgfinanceapi.stock.ApiStocksResponse
import com.omouravictor.ratesbr.data.network.hgfinanceapi.stock.toListStockEntity
import com.omouravictor.ratesbr.data.repository.StocksRepository
import com.omouravictor.ratesbr.presenter.base.DataSource
import com.omouravictor.ratesbr.presenter.base.UiResultStatus
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StocksViewModel @ViewModelInject constructor(
    private val stocksRepository: StocksRepository
) : ViewModel() {

    val stocksResult = MutableLiveData<UiResultStatus<Pair<List<StockUiModel>, DataSource>>>()
    private val apiFields = "stocks"

    init {
        getStocks()
    }

    fun getStocks() {
        viewModelScope.launch(Dispatchers.IO) {
            stocksRepository.getRemoteStocks(apiFields).collect { result ->
                when (result) {
                    is NetworkResultStatus.Success -> handleNetworkSuccessResult(result.data)
                    is NetworkResultStatus.Error -> handleNetworkErrorResult(result.message)
                    is NetworkResultStatus.Loading -> handleNetworkLoadingResult()
                }
            }
        }
    }

    private suspend fun handleNetworkSuccessResult(apiStocksResponse: ApiStocksResponse) {
        val remoteStocksList = apiStocksResponse.toListStockEntity()
        val stocksUiModelList = toStocksUiModelList(remoteStocksList)
        stocksRepository.insertStocks(remoteStocksList)
        postUiResultStatusSuccess(stocksUiModelList, DataSource.NETWORK)
    }

    private fun handleNetworkErrorResult(message: String) {
        val localStocksList = stocksRepository.getLocalStocks()
        if (localStocksList.isNotEmpty()) {
            val stocksUiModelList = toStocksUiModelList(localStocksList)
            postUiResultStatusSuccess(stocksUiModelList, DataSource.LOCAL)
        } else {
            stocksResult.postValue(UiResultStatus.Error(message))
        }
    }

    private fun handleNetworkLoadingResult() {
        stocksResult.postValue(UiResultStatus.Loading)
    }

    private fun toStocksUiModelList(rateEntityList: List<StockEntity>): List<StockUiModel> {
        return rateEntityList.map { it.toStockUiModel() }
    }

    private fun postUiResultStatusSuccess(
        stocksUiModelList: List<StockUiModel>,
        dataSource: DataSource
    ) {
        stocksResult.postValue(
            UiResultStatus.Success(
                Pair(stocksUiModelList, dataSource)
            )
        )
    }
}