package com.omouravictor.ratesnow.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omouravictor.ratesnow.data.local.dao.BitCoinDao
import com.omouravictor.ratesnow.data.local.dao.RateDao
import com.omouravictor.ratesnow.data.local.dao.StockDao
import com.omouravictor.ratesnow.data.local.entity.BitCoinEntity
import com.omouravictor.ratesnow.data.local.entity.CurrencyEntity
import com.omouravictor.ratesnow.data.local.entity.StockEntity

@Database(entities = [CurrencyEntity::class, StockEntity::class, BitCoinEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun rateDao(): RateDao
    abstract fun stockDao(): StockDao
    abstract fun bitCoinDao(): BitCoinDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "rates_now_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}