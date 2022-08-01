package com.omouravictor.ratesnow.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omouravictor.ratesnow.database.entity.RatesEntity

@Dao
interface RateDao {
    @Query("SELECT * FROM rate_table WHERE currency_base = :currency")
    fun getRatesForCurrency(currency: String): RatesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRate(rates: RatesEntity)
}