package com.omouravictor.ratesnow.data.repository

import com.omouravictor.ratesnow.data.network.apilayer.model.RatesNetworkResponse
import com.omouravictor.ratesnow.data.network.apilayer.remote.RatesDataSource
import com.omouravictor.ratesnow.data.network.base.ResultStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RatesRepositoryImpl @Inject constructor(
    private val ratesDataSource: RatesDataSource
) : RatesRepository {
    override suspend fun getRemoteRates(
        base: String,
        symbols: String
    ): Flow<ResultStatus<RatesNetworkResponse>> {
        return ratesDataSource.getRates(base, symbols)
    }
}

interface RatesRepository {
    suspend fun getRemoteRates(
        base: String,
        symbols: String
    ): Flow<ResultStatus<RatesNetworkResponse>>
}