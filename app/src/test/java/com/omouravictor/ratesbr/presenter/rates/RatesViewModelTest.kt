package com.omouravictor.ratesbr.presenter.rates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.omouravictor.ratesbr.data.local.entity.RateEntity
import com.omouravictor.ratesbr.data.local.entity.toRateUiModel
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgfinanceapi.rates.ApiRatesResponse
import com.omouravictor.ratesbr.data.network.hgfinanceapi.rates.ApiRatesResultsItemResponse
import com.omouravictor.ratesbr.data.network.hgfinanceapi.rates.ApiRatesResultsResponse
import com.omouravictor.ratesbr.data.repository.RatesRepository
import com.omouravictor.ratesbr.presenter.base.UiResultStatus
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private lateinit var ratesObserver: Observer<UiResultStatus<List<RateUiModel>>>

    private lateinit var viewModel: RatesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = RatesViewModel(ratesRepository)
        viewModel.ratesResult.observeForever(ratesObserver)
    }

    @Test
    fun `when getRates is called, should fetch remote rates successfully`() {
        runBlocking {
            // Given
            val mockRatesResultsItemResponse = ApiRatesResultsItemResponse(
                1.0, 1.0
            )
            val mockRatesResultsResponse = ApiRatesResultsResponse(
                LinkedHashMap<String, ApiRatesResultsItemResponse>().apply {
                    put("USD", mockRatesResultsItemResponse)
                }
            )
            val mockRatesResponse =
                ApiRatesResponse(mockRatesResultsResponse, Date())
            val mockListRatesEntity =
                listOf(RateEntity("USD", 1.0, 1.0, mockRatesResponse.rateDate))

            val mockSuccessResult = NetworkResultStatus.Success(mockRatesResponse)

            `when`(ratesRepository.getRemoteRates(anyString())).thenReturn(flowOf(mockSuccessResult))

            // When
            viewModel.getRates()

            // Then
            verify(ratesObserver).onChanged(
                UiResultStatus.Success(mockListRatesEntity.map { it.toRateUiModel() })
            )
        }
    }

    @Test
    fun `when getRates is called, should fetch local rates successfully`() {
        runBlocking {
            // Given
            val mockErrorResult =
                NetworkResultStatus.Error(Exception("Falha ao buscar os dados na internet :("))
            val mockListRatesEntity = listOf(RateEntity("USD", 1.0, 1.0, Date()))

            `when`(ratesRepository.getRemoteRates(anyString())).thenReturn(flowOf(mockErrorResult))
            `when`(ratesRepository.getLocalRates()).thenReturn(flowOf(mockListRatesEntity))

            // When
            viewModel.getRates()

            // Then
            verify(ratesObserver).onChanged(
                UiResultStatus.Success(mockListRatesEntity.map { it.toRateUiModel() })
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
                UiResultStatus.Error(mockErrorResult.message)
            )
        }
    }
}