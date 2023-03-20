package com.omouravictor.ratesnow.domain.usecases

import com.omouravictor.ratesnow.data.network.apilayer.model.toRates
import com.omouravictor.ratesnow.data.network.base.ResultStatus
import com.omouravictor.ratesnow.data.repository.RatesRepository
import com.omouravictor.ratesnow.domain.model.Rates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRates @Inject constructor(
    private val ratesRepository: RatesRepository
) : GetRatesUseCase {
    override suspend operator fun invoke(base: String, symbols: String): Flow<ResultStatus<Rates>> {
        return ratesRepository.getRemoteRates(base, symbols).map { resultStatus ->
            when (resultStatus) {
                is ResultStatus.Success -> {
                    ResultStatus.Success(resultStatus.data.toRates())
                }
                is ResultStatus.Error -> {
                    ResultStatus.Error(resultStatus.e)
                }
                is ResultStatus.Loading -> {
                    ResultStatus.Loading
                }
            }
        }
    }
}

interface GetRatesUseCase {
    suspend operator fun invoke(base: String, symbols: String): Flow<ResultStatus<Rates>>
}