package com.omouravictor.ratesbr.presenter.bitcoins

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesbr.data.local.entity.toBitCoinUiModel
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgbrasil.bitcoin.toListBitCoinEntity
import com.omouravictor.ratesbr.data.repository.BitCoinsRepository
import com.omouravictor.ratesbr.presenter.base.UiResultState
import com.omouravictor.ratesbr.presenter.bitcoins.model.BitCoinUiModel
import kotlinx.coroutines.launch

class BitCoinsViewModel @ViewModelInject constructor(
    private val bitCoinsRepository: BitCoinsRepository
) : ViewModel() {
    val bitCoins = MutableLiveData<UiResultState<List<BitCoinUiModel>>>()
    private val fields = "bitcoin"

    init {
        getBitCoins()
    }

    fun getBitCoins() {
        viewModelScope.launch {
            bitCoinsRepository.getRemoteBitCoins(fields).collect { networkResultStatus ->
                when (networkResultStatus) {
                    is NetworkResultStatus.Success -> {
                        val remoteBitCoins = networkResultStatus.data.toListBitCoinEntity()
                        bitCoinsRepository.insertBitCoins(remoteBitCoins)
                        bitCoins.postValue(UiResultState.Success(remoteBitCoins.map { it.toBitCoinUiModel() }))
                    }

                    is NetworkResultStatus.Error -> {
                        bitCoinsRepository.getLocalBitCoins().collect { localBitCoins ->
                            if (localBitCoins.isNotEmpty())
                                bitCoins.postValue(UiResultState.Success(localBitCoins.map { it.toBitCoinUiModel() }))
                            else
                                bitCoins.postValue(UiResultState.Error(networkResultStatus.e))
                        }
                    }

                    is NetworkResultStatus.Loading -> {
                        bitCoins.postValue(UiResultState.Loading)
                    }
                }
            }
        }
    }
}