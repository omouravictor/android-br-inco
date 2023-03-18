package com.omouravictor.ratesnow.ui.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesnow.data.remote.hgbrasil.bitcoin.SourceRequestBitcoinItemModel
import com.omouravictor.ratesnow.data.local.entity.BitCoinEntity
import com.omouravictor.ratesnow.data.repository.BitCoinRepository
import com.omouravictor.ratesnow.utils.DispatcherProvider
import com.omouravictor.ratesnow.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class BitCoinViewModel @ViewModelInject constructor(

    private val repository: BitCoinRepository,
    private val dispatchers: DispatcherProvider

) : ViewModel() {

    val bitCoinsList = repository.getAllFromDb().asLiveData()

    sealed class BitCoinEvent {
        class Failure(val errorText: String) : BitCoinEvent()
        class Success(val text: String = "") : BitCoinEvent()
        object Loading : BitCoinEvent()
    }

    private val bitCoinFields = "bitcoin"
    private val _bitCoins = MutableStateFlow<BitCoinEvent>(BitCoinEvent.Success())
    val bitCoins: StateFlow<BitCoinEvent> = _bitCoins

    fun getBitCoin() {
        viewModelScope.launch(dispatchers.io) {
            _bitCoins.value = BitCoinEvent.Loading
            tryBitCoinsFromApi()
        }
    }

    private suspend fun tryBitCoinsFromApi() {
        when (val request = repository.getAllFromApi(bitCoinFields)) {
            is Resource.Success -> {
                replaceBitCoinOnDb(request.data!!.sourceResultBitcoin.resultsBitcoin)
                _bitCoins.value = BitCoinEvent.Success()
            }
            is Resource.Error -> {
                if (bitCoinsList.value!!.isNotEmpty()) {
                    _bitCoins.value = BitCoinEvent.Success()
                } else {
                    _bitCoins.value = BitCoinEvent.Failure("Verifique sua conexão :(")
                }
            }
        }
    }

    private fun replaceBitCoinOnDb(apiResponse: LinkedHashMap<String, SourceRequestBitcoinItemModel>) {
        val bitCoinsList: MutableList<BitCoinEntity> = mutableListOf()
        apiResponse.forEach {
            bitCoinsList.add(
                BitCoinEntity(
                    it.key,
                    it.value.requestBitcoinBrokerName,
                    it.value.requestBitcoinFormat[0],
                    it.value.requestBitcoinFormat[1],
                    it.value.requestBitcoinBrokerLast,
                    it.value.requestBitcoinBrokerBuy,
                    it.value.requestBitcoinBrokerSell,
                    it.value.requestBitcoinBrokerVariation,
                    Date()
                )
            )
        }
        repository.insertOnDb(bitCoinsList)
    }
}