package com.omouravictor.ratesnow.ui.stock

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesnow.api.hgbrasil.SourceRequestStockItemModel
import com.omouravictor.ratesnow.database.entity.StockEntity
import com.omouravictor.ratesnow.repository.StocksRepository
import com.omouravictor.ratesnow.util.DispatcherProvider
import com.omouravictor.ratesnow.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*

class StockViewModel @ViewModelInject constructor(

    private val repository: StocksRepository,
    private val dispatchers: DispatcherProvider

) : ViewModel() {

    sealed class StockEvent {
        class Success(val stocksList: List<StockEntity>) : StockEvent()
        class Failure(val errorText: String) : StockEvent()
        object Loading : StockEvent()
        object Empty : StockEvent()
    }

    private val stockFields = "stocks"
    private val _stocks = MutableStateFlow<StockEvent>(StockEvent.Empty)
    val stocks: StateFlow<StockEvent> = _stocks

    fun getStocksFromApi() {
        viewModelScope.launch(dispatchers.io) {
            _stocks.value = StockEvent.Loading
            tryStocksFromApi()
        }
    }

    private suspend fun tryStocksFromApi() {
        when (val request = repository.getStocksFromApi(stockFields)) {
            is Resource.Success -> {
                val stocksList = getStocksList(request.data!!.sourceResultStocks.resultsStocks)
                _stocks.value = StockEvent.Success(stocksList)
            }
            is Resource.Error -> {
                tryRatesFromDb()
            }
        }
    }

    private suspend fun tryRatesFromDb() {
        val stocksList = repository.getAllStocksFromDb().first()
        if (stocksList.isNotEmpty()) {
            _stocks.value = StockEvent.Success(stocksList)
        } else {
            _stocks.value = StockEvent.Failure(
                "Não foi possível obter os dados.\nVerifique sua conexão :("
            )
        }
    }

    private fun getStocksList(apiResponse: LinkedHashMap<String, SourceRequestStockItemModel>): MutableList<StockEntity> {
        val stockList: MutableList<StockEntity> = mutableListOf()
        apiResponse.forEach {
            stockList.add(
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
        repository.insertStocksOnDb(stockList)
        return stockList
    }
}