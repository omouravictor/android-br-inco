package com.omouravictor.ratesnow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omouravictor.ratesnow.data.local.entity.BitCoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BitCoinDao {
    @Query("SELECT * FROM bitcoin_table")
    fun getAllBitCoins(): Flow<List<BitCoinEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBitCoin(bitCoins: List<BitCoinEntity>)
}