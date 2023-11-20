package com.omouravictor.brinco.data.repository

import com.omouravictor.brinco.data.local.dao.BitcoinDao
import com.omouravictor.brinco.data.local.entity.BitcoinEntity
import com.omouravictor.brinco.data.network.base.NetworkResultStatus
import com.omouravictor.brinco.data.network.hgfinanceapi.ApiService
import com.omouravictor.brinco.data.network.hgfinanceapi.bitcoin.ApiBitcoinsResponse
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
