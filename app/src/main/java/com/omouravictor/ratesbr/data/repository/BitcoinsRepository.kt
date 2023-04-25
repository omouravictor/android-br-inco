package com.omouravictor.ratesbr.data.repository

import com.omouravictor.ratesbr.data.local.dao.BitcoinDao
import com.omouravictor.ratesbr.data.local.entity.BitcoinEntity
import com.omouravictor.ratesbr.data.network.ApiService
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgbrasil.bitcoin.NetworkBitcoinsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.*

class BitcoinsRepository(
    private val bitcoinDao: BitcoinDao,
    private val apiService: ApiService
) {
    fun getLocalBitcoins(): Flow<List<BitcoinEntity>> = bitcoinDao.getAllBitcoins()

    suspend fun insertBitcoins(bitcoinEntityList: List<BitcoinEntity>) {
        bitcoinDao.insertBitcoins(bitcoinEntityList)
    }

    suspend fun getRemoteBitcoins(fields: String): Flow<NetworkResultStatus<NetworkBitcoinsResponse>> {
        return withContext(Dispatchers.IO) {
            flow {
                emit(NetworkResultStatus.Loading)
                try {
                    val request = apiService.getBitcoins(fields)
                        .apply { bitcoinDate = Date() }
                    emit(NetworkResultStatus.Success(request))
                } catch (e: Exception) {
                    emit(NetworkResultStatus.Error(Exception("Falha ao buscar os dados na internet :(")))
                }
            }
        }
    }
}
