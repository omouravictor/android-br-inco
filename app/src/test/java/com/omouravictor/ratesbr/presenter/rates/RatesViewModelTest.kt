package com.omouravictor.ratesbr.presenter.rates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.omouravictor.ratesbr.data.local.dao.RateDao
import com.omouravictor.ratesbr.data.local.entity.RateEntity
import com.omouravictor.ratesbr.data.local.entity.toRateUiModel
import com.omouravictor.ratesbr.data.network.ApiService
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgbrasil.rates.NetworkRatesResponse
import com.omouravictor.ratesbr.data.network.hgbrasil.rates.NetworkRatesResultsItemResponse
import com.omouravictor.ratesbr.data.network.hgbrasil.rates.NetworkRatesResultsResponse
import com.omouravictor.ratesbr.data.repository.RatesRepository
import com.omouravictor.ratesbr.presenter.base.UiResultState
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RatesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var ratesRepository: RatesRepository

    @Mock
    private lateinit var ratesObserver: Observer<UiResultState<List<RateUiModel>>>

    private lateinit var viewModel: RatesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = RatesViewModel(ratesRepository)
        viewModel.rates.observeForever(ratesObserver)
    }

    @Test
    fun `when getRates is called, should fetch remote rates successfully`() {
        runBlocking {
            // Given
            val mockRatesResultsItemResponse = NetworkRatesResultsItemResponse(
                "USD", 1.0, 1.0, 1.0,
            )
            val mockRatesResultsResponse = NetworkRatesResultsResponse(
                LinkedHashMap<String, NetworkRatesResultsItemResponse>().apply {
                    put("USD", mockRatesResultsItemResponse)
                }
            )
            val mockRatesResponse =
                NetworkRatesResponse("BRL", true, mockRatesResultsResponse, 1.0, true, Date())
            val mockListRatesEntity = listOf(RateEntity("USD", 1.0, mockRatesResponse.rateDate))

            val mockSuccessResult = NetworkResultStatus.Success(mockRatesResponse)

            `when`(ratesRepository.getRemoteRates(anyString())).thenReturn(flowOf(mockSuccessResult))

            // When
            viewModel.getRates()

            // Then
            verify(ratesObserver).onChanged(
                UiResultState.Success(mockListRatesEntity.map { it.toRateUiModel() })
            )
        }
    }

    @Test
    fun `when getRates is called, should fetch local rates successfully`() {
        runBlocking {
            // Given
            val mockErrorResult =
                NetworkResultStatus.Error(Exception("Falha ao buscar os dados na internet :("))
            val mockListRatesEntity = listOf(RateEntity("USD", 1.0, Date()))

            `when`(ratesRepository.getRemoteRates(anyString())).thenReturn(flowOf(mockErrorResult))
            `when`(ratesRepository.getLocalRates()).thenReturn(flowOf(mockListRatesEntity))

            // When
            viewModel.getRates()

            // Then
            verify(ratesObserver).onChanged(
                UiResultState.Success(mockListRatesEntity.map { it.toRateUiModel() })
            )
        }
    }

    @Test
    fun `when getRates is called, should return exception message`() {
        runBlocking {
            // Given
            val mockErrorResult =
                NetworkResultStatus.Error(Exception("Falha ao buscar os dados na internet :("))

            `when`(ratesRepository.getRemoteRates(anyString())).thenReturn(flowOf(mockErrorResult))
            `when`(ratesRepository.getLocalRates()).thenReturn(flowOf(listOf()))

            // When
            viewModel.getRates()

            // Then
            verify(ratesObserver).onChanged(
                UiResultState.Error(mockErrorResult.e)
            )
        }
    }
}