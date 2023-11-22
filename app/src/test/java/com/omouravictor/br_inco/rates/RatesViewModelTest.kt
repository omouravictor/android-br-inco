package com.omouravictor.br_inco.rates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.omouravictor.br_inco.data.network.base.NetworkResultStatus
import com.omouravictor.br_inco.data.network.hgfinanceapi.rates.ApiRatesResponse
import com.omouravictor.br_inco.data.network.hgfinanceapi.rates.ApiRatesResultsItemResponse
import com.omouravictor.br_inco.data.network.hgfinanceapi.rates.ApiRatesResultsResponse
import com.omouravictor.br_inco.data.network.hgfinanceapi.rates.toRatesUiModelList
import com.omouravictor.br_inco.data.repository.RatesRepository
import com.omouravictor.br_inco.presenter.base.DataSource
import com.omouravictor.br_inco.presenter.base.UiResultStatus
import com.omouravictor.br_inco.presenter.rates.RatesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.Date

@ExperimentalCoroutinesApi
class RatesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var ratesRepository: RatesRepository

    private lateinit var viewModel: RatesViewModel
    private val testDispatchers = TestDispatchers()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatchers.standardTestDispatcher)
    }

    @Test
    fun `when getRates is called, should fetch remote rates successfully`() = runTest {
        val mockRateDate = Date()
        val mockApiRatesResponse = ApiRatesResponse(
            ApiRatesResultsResponse(
                linkedMapOf("USD" to ApiRatesResultsItemResponse(5.15, 0.125))
            ),
            mockRateDate
        )

        viewModel = RatesViewModel(ratesRepository, testDispatchers)

        `when`(ratesRepository.getRemoteRates(anyString()))
            .thenReturn(flowOf(NetworkResultStatus.Success(mockApiRatesResponse)))

        viewModel.getRates()

        testDispatchers.standardTestDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            viewModel.ratesResult.value,
            UiResultStatus.Success(
                Pair(mockApiRatesResponse.toRatesUiModelList(), DataSource.NETWORK)
            )
        )
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }
}