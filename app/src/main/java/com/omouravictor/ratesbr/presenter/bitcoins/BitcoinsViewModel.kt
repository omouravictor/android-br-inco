package com.omouravictor.ratesbr.presenter.bitcoins

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesbr.data.local.entity.toBitcoinUiModel
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgfinanceapi.bitcoin.toListBitcoinEntity
import com.omouravictor.ratesbr.data.repository.BitcoinsRepository
import com.omouravictor.ratesbr.presenter.base.UiResultStatus
import com.omouravictor.ratesbr.presenter.bitcoins.model.BitcoinUiModel
import kotlinx.coroutines.launch

class BitcoinsViewModel @ViewModelInject constructor(
    private val bitcoinsRepository: BitcoinsRepository
) : ViewModel() {

    val bitcoinsResult = MutableLiveData<UiResultStatus<List<BitcoinUiModel>>>()

    init {
        getBitcoins()
    }

    fun getBitcoins() {
        viewModelScope.launch {
            bitcoinsRepository.getRemoteBitcoins("bitcoin").collect { networkResultStatus ->
                when (networkResultStatus) {
                    is NetworkResultStatus.Success -> {
                        val remoteBitcoins = networkResultStatus.data.toListBitcoinEntity()
                        bitcoinsRepository.insertBitcoins(remoteBitcoins)
                        bitcoinsResult.postValue(UiResultStatus.Success(remoteBitcoins.map { it.toBitcoinUiModel() }))
                    }

                    is NetworkResultStatus.Error -> {
                        bitcoinsRepository.getLocalBitcoins().collect { localBitcoins ->
                            if (localBitcoins.isNotEmpty())
                                bitcoinsResult.postValue(UiResultStatus.Success(localBitcoins.map { it.toBitcoinUiModel() }))
                            else
                                bitcoinsResult.postValue(UiResultStatus.Error(networkResultStatus.message))
                        }
                    }

                    is NetworkResultStatus.Loading -> {
                        bitcoinsResult.postValue(UiResultStatus.Loading)
                    }
                }
            }
        }
    }
}