package com.omouravictor.ratesnow.repository

import com.omouravictor.ratesnow.api.hgbrasil.bitcoin.BitCoinApi
import com.omouravictor.ratesnow.api.hgbrasil.bitcoin.SourceRequestBitcoinModel
import com.omouravictor.ratesnow.database.AppDataBase
import com.omouravictor.ratesnow.database.entity.BitCoinEntity
import com.omouravictor.ratesnow.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val API_KEY = "0b0d596d"

class BitCoinRepository @Inject constructor(
    private val api: BitCoinApi, private val database: AppDataBase
) {

    suspend fun getBitCoinFromApi(field: String): Resource<SourceRequestBitcoinModel> {
        return try {
            val response = api.getBitCoin(field, API_KEY)
            val result = response.body()
            if (response.isSuccessful && result != null)
                Resource.Success(result)
            else
                Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    fun getAllBitCoinFromDb(): Flow<List<BitCoinEntity>> {
        return database.bitCoinDao().getAllBitCoins()
    }

    fun insertBitCoinOnDb(bitCoinList: List<BitCoinEntity>) {
        database.bitCoinDao().insertBitCoin(bitCoinList)
    }
}