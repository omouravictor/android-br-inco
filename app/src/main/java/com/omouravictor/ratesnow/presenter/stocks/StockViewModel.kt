package com.omouravictor.ratesnow.presenter.stocks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesnow.data.local.entity.StockEntity
import com.omouravictor.ratesnow.data.network.hgbrasil.stock.SourceRequestStockItemModel
import com.omouravictor.ratesnow.data.repository.StocksRepository
import com.omouravictor.ratesnow.utils.DispatcherProvider
import com.omouravictor.ratesnow.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class StockViewModel @ViewModelInject constructor(

    private val repository: StocksRepository,
    private val dispatchers: DispatcherProvider

) : ViewModel() {

    val stocksList = repository.getAllFromDb().asLiveData()

    sealed class StockEvent {
        class Failure(val errorText: String) : StockEvent()
        class Success(val text: String = "") : StockEvent()
        object Loading : StockEvent()
    }

    private val stockFields = "stocks"
    private val _stocks = MutableStateFlow<StockEvent>(StockEvent.Success())
    val stocks: StateFlow<StockEvent> = _stocks

    fun getStocks() {
        viewModelScope.launch(dispatchers.io) {
            _stocks.value = StockEvent.Loading
            tryStocksFromApi()
        }
    }

    private suspend fun tryStocksFromApi() {
        when (val request = repository.getAllFromApi(stockFields)) {
            is Resource.Success -> {
                replaceStocksOnDb(request.data!!.sourceResultStocks.resultsStocks)
                _stocks.value = StockEvent.Success()
            }
            is Resource.Error -> {
                if (stocksList.value!!.isNotEmpty()) {
                    _stocks.value = StockEvent.Success()
                } else {
                    _stocks.value = StockEvent.Failure("Verifique sua conexão :(")
                }
            }
        }
    }

    private fun replaceStocksOnDb(apiResponse: LinkedHashMap<String, SourceRequestStockItemModel>) {
        val stocksList: MutableList<StockEntity> = mutableListOf()
        apiResponse.forEach {
            stocksList.add(
                StockEntity(
                    it.key,
                    it.value.requestStockName,
                    it.value.requestStockLocation,
                    it.value.requestStockPoints,
                    it.value.requestStockVariation,
                    Date()
                )
            )
        }
        repository.insertOnDb(stocksList)
    }
}