package com.azimuton.gamewhiteball.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [History::class], version = 1)
abstract class AppRoomDatabase: RoomDatabase() {
   abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null
       fun getDatabase(context: Context): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "history_database")
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}