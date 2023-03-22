package com.omouravictor.ratesnow.data.repository

import com.omouravictor.ratesnow.data.local.AppDataBase
import com.omouravictor.ratesnow.data.local.entity.BitCoinEntity
import com.omouravictor.ratesnow.data.network.ApiService
import com.omouravictor.ratesnow.data.network.base.NetworkResultStatus
import com.omouravictor.ratesnow.data.network.hgbrasil.bitcoin.SourceRequestBitCoinModel
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
    suspend fun getRemoteBitCoins(filds: String): Flow<NetworkResultStatus<SourceRequestBitCoinModel>> {
        return withContext(Dispatchers.IO) {
            flow {
                emit(NetworkResultStatus.Loading)
                try {
                    val request = apiService.getBitCoins(filds)
                    emit(NetworkResultStatus.Success(request))
                } catch (e: Exception) {
                    emit(NetworkResultStatus.Error(Exception("Falha ao buscar os dados da internet :(")))
                }
            }
        }
    }
}