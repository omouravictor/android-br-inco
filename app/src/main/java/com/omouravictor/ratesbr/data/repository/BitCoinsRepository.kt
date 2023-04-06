package com.omouravictor.ratesbr.data.repository

import com.omouravictor.ratesbr.data.local.dao.BitCoinDao
import com.omouravictor.ratesbr.data.local.entity.BitCoinEntity
import com.omouravictor.ratesbr.data.network.ApiService
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgbrasil.bitcoin.NetworkBitCoinResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class BitCoinsRepository(
    private val bitCoinDao: BitCoinDao,
    private val apiService: ApiService
) {
    fun getLocalBitCoins(): Flow<List<BitCoinEntity>> = bitCoinDao.getAllBitCoins()

    suspend fun insertBitCoins(listBitCoinEntity: List<BitCoinEntity>) {
        bitCoinDao.insertBitCoins(listBitCoinEntity)
    }

    suspend fun getRemoteBitCoins(fields: String): Flow<NetworkResultStatus<NetworkBitCoinResult>> {
        return withContext(Dispatchers.IO) {
            flow {
                emit(NetworkResultStatus.Loading)
                try {
                    val request = apiService.getBitCoins(fields)
                    emit(NetworkResultStatus.Success(request))
                } catch (e: Exception) {
                    emit(NetworkResultStatus.Error(Exception("Falha ao buscar os dados na internet :(")))
                }
            }
        }
    }
}
