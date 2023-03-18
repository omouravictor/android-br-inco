package com.omouravictor.ratesnow.repository

import com.omouravictor.ratesnow.BuildConfig
import com.omouravictor.ratesnow.api.hgbrasil.bitcoin.BitCoinApi
import com.omouravictor.ratesnow.api.hgbrasil.bitcoin.SourceRequestBitcoinModel
import com.omouravictor.ratesnow.database.AppDataBase
import com.omouravictor.ratesnow.database.entity.BitCoinEntity
import com.omouravictor.ratesnow.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BitCoinRepository @Inject constructor(
    private val api: BitCoinApi, private val database: AppDataBase
) {
    suspend fun getAllFromApi(field: String): Resource<SourceRequestBitcoinModel> {
        return try {
            val response = api.getBitCoin(field, BuildConfig.API_KEY_HG)
            val result = response.body()
            if (response.isSuccessful && result != null)
                Resource.Success(result)
            else
                Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    fun getAllFromDb(): Flow<List<BitCoinEntity>> {
        return database.bitCoinDao().getAllBitCoins()
    }

    fun insertOnDb(bitCoinList: List<BitCoinEntity>) {
        database.bitCoinDao().insertBitCoin(bitCoinList)
    }
}