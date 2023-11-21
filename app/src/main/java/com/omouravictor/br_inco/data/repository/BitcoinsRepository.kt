package com.omouravictor.br_inco.data.repository

import com.omouravictor.br_inco.data.local.dao.BitcoinDao
import com.omouravictor.br_inco.data.local.entity.BitcoinEntity
import com.omouravictor.br_inco.data.network.base.NetworkResultStatus
import com.omouravictor.br_inco.data.network.hgfinanceapi.ApiService
import com.omouravictor.br_inco.data.network.hgfinanceapi.bitcoin.ApiBitcoinsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

class BitcoinsRepository(
    private val bitcoinDao: BitcoinDao,
    private val apiService: ApiService
) {
    fun getLocalBitcoins(): List<BitcoinEntity> = bitcoinDao.getAllBitcoins()

    suspend fun insertBitcoins(bitcoinEntityList: List<BitcoinEntity>) {
        bitcoinDao.insertBitcoins(bitcoinEntityList)
    }

    suspend fun getRemoteBitcoins(fields: String): Flow<NetworkResultStatus<ApiBitcoinsResponse>> =
        flow {
            emit(NetworkResultStatus.Loading)
            try {
                val request = apiService.getBitcoins(fields)
                    .apply { bitcoinDate = Date() }
                emit(NetworkResultStatus.Success(request))
            } catch (e: Exception) {
                emit(NetworkResultStatus.Error("Falha ao buscar os dados na internet :("))
            }
        }
}
