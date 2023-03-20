package com.omouravictor.ratesnow.data.repository

import com.omouravictor.ratesnow.BuildConfig
import com.omouravictor.ratesnow.data.local.AppDataBase
import com.omouravictor.ratesnow.data.local.entity.BitCoinEntity
import com.omouravictor.ratesnow.data.network.hgbrasil.bitcoin.BitCoinApi
import com.omouravictor.ratesnow.data.network.hgbrasil.bitcoin.SourceRequestBitcoinModel
import com.omouravictor.ratesnow.utils.Resource
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