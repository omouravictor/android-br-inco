package com.omouravictor.ratesnow.data.repository

import com.omouravictor.ratesnow.data.local.AppDataBase
import com.omouravictor.ratesnow.data.local.entity.CurrencyEntity
import com.omouravictor.ratesnow.data.network.base.NetworkResultStatus
import com.omouravictor.ratesnow.data.network.ApiService
import com.omouravictor.ratesnow.data.network.hgbrasil.rates.SourceRequestCurrencyModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.lang.Exception

class RatesLocalRepository(private val database: AppDataBase) {
    fun getLocalRates(): Flow<List<CurrencyEntity>> = database.rateDao().getAllRates()

    suspend fun insertRates(rates: List<CurrencyEntity>) {
        database.rateDao().insertRates(rates)
    }
}

class RatesApiRepository(private val apiService: ApiService) {
    suspend fun getRemoteRates(field: String): Flow<NetworkResultStatus<SourceRequestCurrencyModel>> {
        return withContext(Dispatchers.IO) {
            flow {
                emit(NetworkResultStatus.Loading)
                try {
                    val request = apiService.getCurrencies(field)
                    emit(NetworkResultStatus.Success(request))
                } catch (e: Exception) {
                    emit(NetworkResultStatus.Error(e))
                }
            }
        }
    }
}