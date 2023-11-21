package com.omouravictor.br_inco.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omouravictor.br_inco.data.local.dao.BitcoinDao
import com.omouravictor.br_inco.data.local.dao.RateDao
import com.omouravictor.br_inco.data.local.dao.StockDao
import com.omouravictor.br_inco.data.local.entity.BitcoinEntity
import com.omouravictor.br_inco.data.local.entity.RateEntity
import com.omouravictor.br_inco.data.local.entity.StockEntity

@Database(entities = [RateEntity::class, StockEntity::class, BitcoinEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun rateDao(): RateDao
    abstract fun stockDao(): StockDao
    abstract fun bitcoinDao(): BitcoinDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "rates_br_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}