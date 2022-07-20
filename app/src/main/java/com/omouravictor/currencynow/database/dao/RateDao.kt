package com.omouravictor.currencynow.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omouravictor.currencynow.database.entity.RatesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RateDao {
    @Query("SELECT * FROM rate_table")
    fun getAllRates(): Flow<List<RatesEntity>>

    @Query("SELECT * FROM rate_table WHERE currency_base = :currency")
    fun getRatesForCurrency(currency: String): RatesEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRate(rates: RatesEntity)

    @Query("DELETE FROM rate_table")
    suspend fun deleteAllRates()
}