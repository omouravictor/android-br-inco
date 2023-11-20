package com.omouravictor.brinco.presenter.stocks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.brinco.data.local.entity.toStockUiModel
import com.omouravictor.brinco.data.network.base.NetworkResultStatus
import com.omouravictor.brinco.data.network.hgfinanceapi.stock.ApiStocksResponse
import com.omouravictor.brinco.data.network.hgfinanceapi.stock.toStocksEntityList
import com.omouravictor.brinco.data.network.hgfinanceapi.stock.toStocksUiModelList
import com.omouravictor.brinco.data.repository.StocksRepository
import com.omouravictor.brinco.presenter.base.DataSource
import com.omouravictor.brinco.presenter.base.UiResultStatus
import com.omouravictor.brinco.presenter.stocks.model.StockUiModel
import com.omouravictor.brinco.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
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