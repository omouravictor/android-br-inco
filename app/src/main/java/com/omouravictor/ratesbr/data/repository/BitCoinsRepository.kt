package com.omouravictor.ratesbr.data.repository

import com.omouravictor.ratesbr.data.local.AppDataBase
import com.omouravictor.ratesbr.data.local.entity.BitCoinEntity
import com.omouravictor.ratesbr.data.network.ApiService
import com.omouravictor.ratesbr.data.network.base.NetworkResultStatus
import com.omouravictor.ratesbr.data.network.hgbrasil.bitcoin.NetworkBitCoinResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class BitCoinsLocalRepository(private val database: AppDataBase) {
    fun getLocalBitCoins(): Flow<List<BitCoinEntity>> = database.bitCoinDao().getAllBitCoins()

    suspend fun insertBitCoins(listBitCoinEntity: List<BitCoinEntity>) {
        database.bitCoinDao().insertBitCoins(listBitCoinEntity)
    }
}

class BitCoinsApiRepository(private val apiService: ApiService) {
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