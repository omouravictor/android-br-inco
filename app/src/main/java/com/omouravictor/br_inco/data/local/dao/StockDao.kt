package com.omouravictor.br_inco.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omouravictor.br_inco.data.local.entity.StockEntity

@Dao
interface StockDao {
    @Query("SELECT * FROM stock_table")
    fun getAllStocks(): List<StockEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStocks(listStockEntity: List<StockEntity>)
}