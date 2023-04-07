package com.omouravictor.ratesbr.presenter.rates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.omouravictor.ratesbr.data.local.entity.RateEntity
import com.omouravictor.ratesbr.data.local.entity.toRateUiModel
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgbrasil.rates.NetworkRatesResponse
import com.omouravictor.ratesbr.data.network.hgbrasil.rates.NetworkRatesResultsItemResponse
import com.omouravictor.ratesbr.data.network.hgbrasil.rates.NetworkRatesResultsResponse
import com.omouravictor.ratesbr.data.repository.RatesRepository
import com.omouravictor.ratesbr.presenter.base.UiResultState
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
            val ratesResultsItemResponse = NetworkRatesResultsItemResponse(
                "USD", 1.0, 1.0, 1.0,
            )
            val ratesResultsResponse = NetworkRatesResultsResponse(
                LinkedHashMap<String, NetworkRatesResultsItemResponse>().apply {
                    put("USD", ratesResultsItemResponse)
                }
            )
            val ratesResponse =
                NetworkRatesResponse("BRL", true, ratesResultsResponse, 1.0, true, Date())
            val successResult = NetworkResultStatus.Success(ratesResponse)
            val mockRatesEntity = RateEntity("USD", 1.0, ratesResponse.rateDate)

            `when`(ratesRepository.getRemoteRates(anyString())).thenReturn(flowOf(successResult))

            // When
            viewModel.getRates()

            // Then
            verify(ratesObserver).onChanged(
                UiResultState.Success(listOf(mockRatesEntity).map { it.toRateUiModel() })
            )
        }
    }
}