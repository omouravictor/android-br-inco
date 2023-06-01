package com.omouravictor.ratesbr.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omouravictor.ratesbr.data.local.entity.BitcoinEntity

@Dao
interface BitcoinDao {
    @Query("SELECT * FROM bitcoin_table")
    fun getAllBitcoins(): List<BitcoinEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBitcoins(listBitcoinEntity: List<BitcoinEntity>)
}