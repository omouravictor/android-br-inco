package com.omouravictor.br_inco.data.repository

import com.omouravictor.br_inco.data.local.dao.StockDao
import com.omouravictor.br_inco.data.local.entity.StockEntity
import com.omouravictor.br_inco.data.network.base.NetworkResultStatus
import com.omouravictor.br_inco.data.network.hgfinanceapi.ApiService
import com.omouravictor.br_inco.data.network.hgfinanceapi.stock.ApiStocksResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

class StocksRepository(
    private val stockDao: StockDao,
    private val apiService: ApiService
) {
    fun getLocalStocks(): List<StockEntity> = stockDao.getAllStocks()

    suspend fun insertStocks(stockEntityList: List<StockEntity>) {
        stockDao.insertStocks(stockEntityList)
    }

    suspend fun getRemoteStocks(fields: String): Flow<NetworkResultStatus<ApiStocksResponse>> =
        flow {
            emit(NetworkResultStatus.Loading)
            try {
                val request = apiService.getStocks(fields)
                    .apply { stockDate = Date() }
                emit(NetworkResultStatus.Success(request))
            } catch (e: Exception) {
                emit(NetworkResultStatus.Error("Falha ao buscar os dados na internet :("))
            }
        }
}