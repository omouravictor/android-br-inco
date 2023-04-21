package com.omouravictor.ratesbr.data.repository

import com.omouravictor.ratesbr.data.local.dao.BitCoinDao
import com.omouravictor.ratesbr.data.local.entity.BitCoinEntity
import com.omouravictor.ratesbr.data.network.ApiService
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgbrasil.bitcoin.NetworkBitCoinsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.*

class BitCoinsRepository(
    private val bitCoinDao: BitCoinDao,
    private val apiService: ApiService
) {
    fun getLocalBitCoins(): Flow<List<BitCoinEntity>> = bitCoinDao.getAllBitCoins()

    suspend fun insertBitCoins(bitCoinEntityList: List<BitCoinEntity>) {
        bitCoinDao.insertBitCoins(bitCoinEntityList)
    }

    suspend fun getRemoteBitCoins(fields: String): Flow<NetworkResultStatus<NetworkBitCoinsResponse>> {
        return withContext(Dispatchers.IO) {
            flow {
                emit(NetworkResultStatus.Loading)
                try {
                    val request = apiService.getBitCoins(fields)
                        .apply { bitcoinDate = Date() }
                    emit(NetworkResultStatus.Success(request))
                } catch (e: Exception) {
                    emit(NetworkResultStatus.Error(Exception("Falha ao buscar os dados na internet :(")))
                }
            }
        }
    }
}
