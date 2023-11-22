package com.omouravictor.br_inco.presenter.bitcoins

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omouravictor.br_inco.data.local.entity.toBitcoinUiModel
import com.omouravictor.br_inco.data.network.base.NetworkResultStatus
import com.omouravictor.br_inco.data.network.hgfinanceapi.bitcoin.ApiBitcoinsResponse
import com.omouravictor.br_inco.data.network.hgfinanceapi.bitcoin.toBitcoinsEntityList
import com.omouravictor.br_inco.data.network.hgfinanceapi.bitcoin.toBitcoinsUiModelList
import com.omouravictor.br_inco.data.repository.BitcoinsRepository
import com.omouravictor.br_inco.presenter.base.DataSource
import com.omouravictor.br_inco.presenter.base.UiResultStatus
import com.omouravictor.br_inco.presenter.bitcoins.model.BitcoinUiModel
import com.omouravictor.br_inco.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BitcoinsViewModel @Inject constructor(
    private val bitcoinsRepository: BitcoinsRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    val bitcoinsResult = MutableLiveData<UiResultStatus<Pair<List<BitcoinUiModel>, DataSource>>>()
    private val apiFields = "bitcoin"

    init {
        getBitcoins()
    }

    fun getBitcoins() {
        viewModelScope.launch(dispatchers.io) {
            bitcoinsRepository.getRemoteBitcoins(apiFields).collectLatest { result ->
                when (result) {
                    is NetworkResultStatus.Success -> handleNetworkSuccessResult(result.data)
                    is NetworkResultStatus.Error -> handleNetworkErrorResult(result.message)
                    is NetworkResultStatus.Loading -> handleNetworkLoadingResult()
                }
            }
        }
    }

    private fun postUiResultStatusSuccess(
        bitcoinsUiModelList: List<BitcoinUiModel>,
        dataSource: DataSource
    ) {
        bitcoinsResult.postValue(UiResultStatus.Success(Pair(bitcoinsUiModelList, dataSource)))
    }

    private suspend fun handleNetworkSuccessResult(apiBitcoinsResponse: ApiBitcoinsResponse) {
        bitcoinsRepository.insertBitcoins(apiBitcoinsResponse.toBitcoinsEntityList())
        postUiResultStatusSuccess(apiBitcoinsResponse.toBitcoinsUiModelList(), DataSource.NETWORK)
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

}