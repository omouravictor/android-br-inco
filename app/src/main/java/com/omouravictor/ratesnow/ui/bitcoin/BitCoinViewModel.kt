package com.omouravictor.ratesnow.ui.bitcoin

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesnow.api.hgbrasil.bitcoin.SourceRequestBitcoinItemModel
import com.omouravictor.ratesnow.database.entity.BitCoinEntity
import com.omouravictor.ratesnow.repository.BitCoinRepository
import com.omouravictor.ratesnow.util.DispatcherProvider
import com.omouravictor.ratesnow.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class BitCoinViewModel @ViewModelInject constructor(

    private val repository: BitCoinRepository,
    private val dispatchers: DispatcherProvider

) : ViewModel() {

    val bitCoinsList = repository.getAllBitCoinFromDb().asLiveData()

    sealed class BitCoinEvent {
        class Failure(val errorText: String) : BitCoinEvent()
        object Success : BitCoinEvent()
        object Loading : BitCoinEvent()
    }

    private val bitCoinFields = "bitcoin"
    private val _bitCoins = MutableStateFlow<BitCoinEvent>(BitCoinEvent.Success)
    val bitCoins: StateFlow<BitCoinEvent> = _bitCoins

    fun getBitCoin() {
        viewModelScope.launch(dispatchers.io) {
            _bitCoins.value = BitCoinEvent.Loading
            tryBitCoinsFromApi()
        }
    }

    private suspend fun tryBitCoinsFromApi() {
        when (val request = repository.getBitCoinFromApi(bitCoinFields)) {
            is Resource.Success -> {
                replaceBitCoinOnDb(request.data!!.sourceResultBitcoin.resultsBitcoin)
                _bitCoins.value = BitCoinEvent.Success
            }
            is Resource.Error -> {
                _bitCoins.value = BitCoinEvent.Failure("Verifique sua conexão :(")
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
        repository.insertBitCoinOnDb(bitCoinsList)
    }
}