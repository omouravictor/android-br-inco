package com.omouravictor.ratesbr.data.repository

import com.omouravictor.ratesbr.data.local.AppDataBase
import com.omouravictor.ratesbr.data.local.entity.RateEntity
import com.omouravictor.ratesbr.data.network.ApiService
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgbrasil.rates.SourceRequestCurrencyModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class RatesLocalRepository(private val database: AppDataBase) {
    fun getLocalRates(): Flow<List<RateEntity>> = database.rateDao().getAllRates()

    suspend fun insertRates(listRateEntity: List<RateEntity>) {
        database.rateDao().insertRates(listRateEntity)
    }
}

class RatesApiRepository(private val apiService: ApiService) {
    suspend fun getRemoteRates(currencies: String): Flow<NetworkResultStatus<SourceRequestCurrencyModel>> {
        return withContext(Dispatchers.IO) {
            flow {
                emit(NetworkResultStatus.Loading)
                try {
                    val request = apiService.getRates(currencies)
                    emit(NetworkResultStatus.Success(request))
                } catch (e: Exception) {
                    emit(NetworkResultStatus.Error(Exception("Falha ao buscar os dados da internet :(")))
                }
            }
        }
    }
}