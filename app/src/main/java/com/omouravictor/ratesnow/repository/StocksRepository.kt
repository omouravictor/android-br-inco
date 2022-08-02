package com.omouravictor.ratesnow.repository

import com.omouravictor.ratesnow.api.hgbrasil.stock.SourceRequestStockModel
import com.omouravictor.ratesnow.api.hgbrasil.stock.StocksApi
import com.omouravictor.ratesnow.database.AppDataBase
import com.omouravictor.ratesnow.database.entity.StockEntity
import com.omouravictor.ratesnow.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val API_KEY = "0b0d596d"

class StocksRepository @Inject constructor(
    private val api: StocksApi, private val database: AppDataBase
) {

    suspend fun getStocksFromApi(field: String): Resource<SourceRequestStockModel> {
        return try {
            val response = api.getStocks(field, API_KEY)
            val result = response.body()
            if (response.isSuccessful && result != null)
                Resource.Success(result)
            else
                Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    fun getAllStocksFromDb(): Flow<List<StockEntity>> {
        return database.stockDao().getAllStocks()
    }

    fun insertStocksOnDb(stocksList: List<StockEntity>) {
        database.stockDao().insertStock(stocksList)
    }
}