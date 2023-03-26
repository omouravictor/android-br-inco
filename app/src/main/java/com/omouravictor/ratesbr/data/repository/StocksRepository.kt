package com.omouravictor.ratesbr.data.repository

import com.omouravictor.ratesbr.data.local.dao.StockDao
import com.omouravictor.ratesbr.data.local.entity.StockEntity
import com.omouravictor.ratesbr.data.network.ApiService
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgbrasil.stock.SourceRequestStockModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class StocksLocalRepository(private val stockDao: StockDao) {
    fun getLocalStocks(): Flow<List<StockEntity>> = stockDao.getAllStocks()

    suspend fun insertStocks(listStockEntity: List<StockEntity>) {
        stockDao.insertStocks(listStockEntity)
    }
}

class StocksApiRepository(private val apiService: ApiService) {
    suspend fun getRemoteStocks(fields: String): Flow<NetworkResultStatus<SourceRequestStockModel>> {
        return withContext(Dispatchers.IO) {
            flow {
                emit(NetworkResultStatus.Loading)
                try {
                    val request = apiService.getStocks(fields)
                    emit(NetworkResultStatus.Success(request))
                } catch (e: Exception) {
                    emit(NetworkResultStatus.Error(Exception("Falha ao buscar os dados na internet :(")))
                }
            }
        }
    }
}