package com.omouravictor.ratesnow.repository

import com.omouravictor.ratesnow.BuildConfig
import com.omouravictor.ratesnow.api.hgbrasil.stock.SourceRequestStockModel
import com.omouravictor.ratesnow.api.hgbrasil.stock.StocksApi
import com.omouravictor.ratesnow.database.AppDataBase
import com.omouravictor.ratesnow.database.entity.StockEntity
import com.omouravictor.ratesnow.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StocksRepository @Inject constructor(
    private val api: StocksApi, private val database: AppDataBase
) {
    suspend fun getAllFromApi(field: String): Resource<SourceRequestStockModel> {
        return try {
            val response = api.getStocks(field, BuildConfig.API_KEY_HG)
            val result = response.body()
            if (response.isSuccessful && result != null)
                Resource.Success(result)
            else
                Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    fun getAllFromDb(): Flow<List<StockEntity>> {
        return database.stockDao().getAllStocks()
    }

    fun insertOnDb(stocksList: List<StockEntity>) {
        database.stockDao().insertStock(stocksList)
    }
}