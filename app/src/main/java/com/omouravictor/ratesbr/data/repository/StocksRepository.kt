package com.omouravictor.ratesbr.data.repository

import com.omouravictor.ratesbr.data.local.dao.StockDao
import com.omouravictor.ratesbr.data.local.entity.StockEntity
import com.omouravictor.ratesbr.data.network.ApiService
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgbrasil.stock.NetworkStocksResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.*

class StocksRepository(
    private val stockDao: StockDao,
    private val apiService: ApiService
) {
    fun getLocalStocks(): Flow<List<StockEntity>> = stockDao.getAllStocks()

    suspend fun insertStocks(stockEntityList: List<StockEntity>) {
        stockDao.insertStocks(stockEntityList)
    }

    suspend fun getRemoteStocks(fields: String): Flow<NetworkResultStatus<NetworkStocksResponse>> {
        return withContext(Dispatchers.IO) {
            flow {
                emit(NetworkResultStatus.Loading)
                try {
                    val request = apiService.getStocks(fields)
                        .apply { stockDate = Date() }
                    emit(NetworkResultStatus.Success(request))
                } catch (e: Exception) {
                    emit(NetworkResultStatus.Error(Exception("Falha ao buscar os dados na internet :(")))
                }
            }
        }
    }
}