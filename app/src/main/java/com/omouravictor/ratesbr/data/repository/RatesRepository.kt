package com.omouravictor.ratesbr.data.repository

import com.omouravictor.ratesbr.data.local.dao.RateDao
import com.omouravictor.ratesbr.data.local.entity.RateEntity
import com.omouravictor.ratesbr.data.network.ApiService
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgbrasil.rates.NetworkRatesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class RatesRepository(
    private val rateDao: RateDao,
    private val apiService: ApiService
) {
    fun getLocalRates(): Flow<List<RateEntity>> = rateDao.getAllRates()

    suspend fun insertRates(listRateEntity: List<RateEntity>) {
        rateDao.insertRates(listRateEntity)
    }

    suspend fun getRemoteRates(currencies: String): Flow<NetworkResultStatus<NetworkRatesResponse>> {
        return withContext(Dispatchers.IO) {
            flow {
                emit(NetworkResultStatus.Loading)
                try {
                    val request = apiService.getRates(currencies)
                    emit(NetworkResultStatus.Success(request))
                } catch (e: Exception) {
                    emit(NetworkResultStatus.Error(Exception("Falha ao buscar os dados na internet :(")))
                }
            }
        }
    }
}