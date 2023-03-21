package com.omouravictor.ratesnow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omouravictor.ratesnow.data.local.entity.RateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RateDao {
    @Query("SELECT * FROM rate_table")
    fun getAllRates(): Flow<List<RateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(listRateEntity: List<RateEntity>)
}