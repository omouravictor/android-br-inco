package com.omouravictor.br_inco.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omouravictor.br_inco.data.local.entity.RateEntity

@Dao
interface RateDao {
    @Query("SELECT * FROM rate_table")
    fun getAllRates(): List<RateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(listRateEntity: List<RateEntity>)
}