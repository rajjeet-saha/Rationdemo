package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        HouseholdEntity::class,
        MemberEntity::class,
        EntitlementEntity::class,
        ShopStockEntity::class,
        OfflineDraftEntity::class,
        GrievanceEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RationSaathiDatabase : RoomDatabase() {
    abstract fun dao(): RationSaathiDao

    companion object {
        @Volatile
        private var INSTANCE: RationSaathiDatabase? = null

        fun getInstance(context: Context): RationSaathiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RationSaathiDatabase::class.java,
                    "rationsaathi.db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
