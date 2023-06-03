package com.omouravictor.ratesbr.presenter.bitcoins

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.ratesbr.data.local.entity.toBitcoinUiModel
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgfinanceapi.bitcoin.ApiBitcoinsResponse
import com.omouravictor.ratesbr.data.network.hgfinanceapi.bitcoin.toBitcoinsEntityList
import com.omouravictor.ratesbr.data.network.hgfinanceapi.bitcoin.toBitcoinsUiModelList
import com.omouravictor.ratesbr.data.repository.BitcoinsRepository
import com.omouravictor.ratesbr.presenter.base.DataSource
import com.omouravictor.ratesbr.presenter.base.UiResultStatus
import com.omouravictor.ratesbr.presenter.bitcoins.model.BitcoinUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BitcoinsViewModel @ViewModelInject constructor(
    private val bitcoinsRepository: BitcoinsRepository
) : ViewModel() {

    val bitcoinsResult = MutableLiveData<UiResultStatus<Pair<List<BitcoinUiModel>, DataSource>>>()
    private val apiFields = "bitcoin"

    init {
        getBitcoins()
    }

    fun getBitcoins() {
        viewModelScope.launch(Dispatchers.IO) {
            bitcoinsRepository.getRemoteBitcoins(apiFields).collect { result ->
                when (result) {
                    is NetworkResultStatus.Success -> handleNetworkSuccessResult(result.data)
                    is NetworkResultStatus.Error -> handleNetworkErrorResult(result.message)
                    is NetworkResultStatus.Loading -> handleNetworkLoadingResult()
                }
            }
        }
    }

    private suspend fun handleNetworkSuccessResult(apiBitcoinsResponse: ApiBitcoinsResponse) {
        val remoteBitcoinsEntityList = apiBitcoinsResponse.toBitcoinsEntityList()
        val remoteBitcoinsUiModelList = apiBitcoinsResponse.toBitcoinsUiModelList()
        bitcoinsRepository.insertBitcoins(remoteBitcoinsEntityList)
        postUiResultStatusSuccess(remoteBitcoinsUiModelList, DataSource.NETWORK)
    }

    private fun handleNetworkErrorResult(message: String) {
        val localBitcoinsEntityList = bitcoinsRepository.getLocalBitcoins()
        if (localBitcoinsEntityList.isNotEmpty()) {
            val localBitcoinsUiModelList = localBitcoinsEntityList.map { it.toBitcoinUiModel() }
            postUiResultStatusSuccess(localBitcoinsUiModelList, DataSource.LOCAL)
        } else {
            bitcoinsResult.postValue(UiResultStatus.Error(message))
        }
    }

    private fun handleNetworkLoadingResult() {
        bitcoinsResult.postValue(UiResultStatus.Loading)
    }

    private fun postUiResultStatusSuccess(
        bitcoinsUiModelList: List<BitcoinUiModel>,
        dataSource: DataSource
    ) {
        bitcoinsResult.postValue(
            UiResultStatus.Success(
                Pair(bitcoinsUiModelList, dataSource)
            )
        )
    }
}