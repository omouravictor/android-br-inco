package com.omouravictor.ratesnow.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omouravictor.ratesnow.database.entity.StockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * FROM stock_table")
    fun getAllStocks(): Flow<List<StockEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStock(stocks: List<StockEntity>)
}