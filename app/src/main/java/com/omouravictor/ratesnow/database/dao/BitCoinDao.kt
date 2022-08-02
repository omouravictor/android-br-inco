package com.omouravictor.ratesnow.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omouravictor.ratesnow.database.entity.BitCoinEntity
import com.omouravictor.ratesnow.database.entity.StockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BitCoinDao {
    @Query("SELECT * FROM bitcoin_table")
    fun getAllBitCoins(): Flow<List<BitCoinEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBitCoin(bitCoins: List<BitCoinEntity>)
}