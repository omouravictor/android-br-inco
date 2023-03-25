package com.omouravictor.ratesnow.presenter.stocks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesnow.data.local.entity.toStockUiModel
import com.omouravictor.ratesnow.data.network.base.NetworkResultStatus
import com.omouravictor.ratesnow.data.network.hgbrasil.stock.toListStockEntity
import com.omouravictor.ratesnow.data.repository.StocksApiRepository
import com.omouravictor.ratesnow.data.repository.StocksLocalRepository
import com.omouravictor.ratesnow.presenter.base.UiResultState
import com.omouravictor.ratesnow.presenter.stocks.model.StockUiModel
import kotlinx.coroutines.launch

class StocksViewModel @ViewModelInject constructor(
    private val stocksLocalRepository: StocksLocalRepository,
    private val stocksApiRepository: StocksApiRepository
) : ViewModel() {
    val stocks = MutableLiveData<UiResultState<List<StockUiModel>>>()
    private val fields = "stocks"

    init {
        getStocks()
    }

    fun getStocks() {
        viewModelScope.launch {
            stocksApiRepository.getRemoteStocks(fields).collect { networkResultStatus ->
                when (networkResultStatus) {
                    is NetworkResultStatus.Success -> {
                        val remoteStocks = networkResultStatus.data.toListStockEntity()
                        stocksLocalRepository.insertStocks(remoteStocks)
                        stocks.postValue(UiResultState.Success(remoteStocks.map { it.toStockUiModel() }))
                    }

                    is NetworkResultStatus.Error -> {
                        stocksLocalRepository.getLocalStocks().collect { localStocks ->
                            if (localStocks.isNotEmpty())
                                stocks.postValue(UiResultState.Success(localStocks.map { it.toStockUiModel() }))
                            else
                                stocks.postValue(UiResultState.Error(networkResultStatus.e))
                        }
                    }

                    is NetworkResultStatus.Loading -> {
                        stocks.postValue(UiResultState.Loading)
                    }
                }
            }
        }
    }
}