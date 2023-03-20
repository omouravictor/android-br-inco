package com.omouravictor.ratesnow.data.network.apilayer.remote

import com.omouravictor.ratesnow.data.network.apilayer.RatesApi
import com.omouravictor.ratesnow.data.network.apilayer.model.RatesNetworkResponse
import com.omouravictor.ratesnow.data.network.base.ResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RetrofitRatesDataSource @Inject constructor(
    private val ratesApi: RatesApi
) : RatesDataSource {
    @Suppress("TooGenericExceptionCaught")
    override suspend fun getRates(
        base: String,
        symbols: String
    ): Flow<ResultStatus<RatesNetworkResponse>> {
        return withContext(Dispatchers.IO) {
            flow {
                emit(ResultStatus.Loading)
                try {
                    val request = ratesApi.getRates(base, symbols)
                    emit(ResultStatus.Success(request))
                } catch (e: Exception) {
                    emit(ResultStatus.Error(e))
                }
            }
        }
    }
}

interface RatesDataSource {
    suspend fun getRates(base: String, symbols: String): Flow<ResultStatus<RatesNetworkResponse>>
}