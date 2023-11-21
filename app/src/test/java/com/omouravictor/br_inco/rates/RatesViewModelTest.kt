package com.omouravictor.br_inco.rates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.omouravictor.br_inco.data.network.base.NetworkResultStatus
import com.omouravictor.br_inco.data.network.hgfinanceapi.rates.ApiRatesResponse
import com.omouravictor.br_inco.data.network.hgfinanceapi.rates.ApiRatesResultsItemResponse
import com.omouravictor.br_inco.data.network.hgfinanceapi.rates.ApiRatesResultsResponse
import com.omouravictor.br_inco.data.network.hgfinanceapi.rates.toRatesUiModelList
import com.omouravictor.br_inco.data.repository.RatesRepository
import com.omouravictor.br_inco.presenter.base.DataSource
import com.omouravictor.br_inco.presenter.base.UiResultStatus
import com.omouravictor.br_inco.presenter.rates.RatesViewModel
import com.omouravictor.br_inco.presenter.rates.model.RateUiModel
import com.omouravictor.br_inco.util.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.Date

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class RatesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var ratesRepository: RatesRepository

    @Mock
    private lateinit var ratesObserver: Observer<UiResultStatus<Pair<List<RateUiModel>, DataSource>>>
    private lateinit var mockApiRatesResponse: ApiRatesResponse
    private lateinit var viewModel: RatesViewModel
    private var rateDate = Date()
    private var dispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockApiRatesResponse = ApiRatesResponse(
            ApiRatesResultsResponse(
                linkedMapOf("USD" to ApiRatesResultsItemResponse(5.15, 0.125))
            ),
            rateDate
        )
        viewModel = RatesViewModel(ratesRepository, dispatcherProvider)
        viewModel.ratesResult.observeForever(ratesObserver)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when getRates is called, should fetch remote rates successfully`() {
        runBlocking {
            // Given
            Mockito.`when`(ratesRepository.getRemoteRates(anyString()))
                .thenReturn(flowOf(NetworkResultStatus.Success(mockApiRatesResponse)))

            // When
            viewModel.getRates()

            // Then
            Mockito.verify(ratesObserver).onChanged(
                UiResultStatus.Success(
                    Pair(
                        mockApiRatesResponse.toRatesUiModelList(),
                        DataSource.NETWORK
                    )
                )
            )
        }
    }
}