package com.omouravictor.ratesnow.presenter.bitcoins

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesnow.data.local.entity.toBitCoinUiModel
import com.omouravictor.ratesnow.data.network.base.NetworkResultStatus
import com.omouravictor.ratesnow.data.network.hgbrasil.bitcoin.toListBitCoinEntity
import com.omouravictor.ratesnow.data.repository.BitCoinsApiRepository
import com.omouravictor.ratesnow.data.repository.BitCoinsLocalRepository
import com.omouravictor.ratesnow.presenter.base.UiResultState
import com.omouravictor.ratesnow.presenter.bitcoins.model.BitCoinUiModel
import kotlinx.coroutines.launch

class BitCoinsViewModel @ViewModelInject constructor(
    private val bitCoinsLocalRepository: BitCoinsLocalRepository,
    private val bitCoinsApiRepository: BitCoinsApiRepository
) : ViewModel() {
    val bitCoins = MutableLiveData<UiResultState<List<BitCoinUiModel>>>()
    private val filds = "bitcoin"

    init {
        getBitCoins()
    }

    fun getBitCoins() {
        viewModelScope.launch {
            bitCoinsApiRepository.getRemoteBitCoins(filds).collect { networkResultStatus ->
                when (networkResultStatus) {
                    is NetworkResultStatus.Success -> {
                        val remoteBitCoins = networkResultStatus.data.toListBitCoinEntity()
                        bitCoinsLocalRepository.insertBitCoins(remoteBitCoins)
                        bitCoins.postValue(UiResultState.Success(remoteBitCoins.map { it.toBitCoinUiModel() }))
                    }

                    is NetworkResultStatus.Error -> {
                        bitCoinsLocalRepository.getLocalBitCoins().collect { localBitCoins ->
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