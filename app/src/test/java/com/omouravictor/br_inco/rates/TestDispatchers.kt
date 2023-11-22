package com.omouravictor.br_inco.rates

import com.omouravictor.br_inco.util.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher

@ExperimentalCoroutinesApi
class TestDispatchers : DispatcherProvider {
    val standardTestDispatcher = StandardTestDispatcher()
    override val main: CoroutineDispatcher
        get() = standardTestDispatcher
    override val io: CoroutineDispatcher
        get() = standardTestDispatcher
    override val default: CoroutineDispatcher
        get() = standardTestDispatcher
    override val unconfined: CoroutineDispatcher
        get() = standardTestDispatcher
}