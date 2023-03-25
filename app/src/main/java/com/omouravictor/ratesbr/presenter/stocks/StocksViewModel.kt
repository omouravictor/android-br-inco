package com.omouravictor.ratesbr.presenter.stocks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesbr.data.local.entity.toStockUiModel
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgbrasil.stock.toListStockEntity
import com.omouravictor.ratesbr.data.repository.StocksApiRepository
import com.omouravictor.ratesbr.data.repository.StocksLocalRepository
import com.omouravictor.ratesbr.presenter.base.UiResultState
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
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