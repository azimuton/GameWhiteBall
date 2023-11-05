package com.azimuton.gamewhiteball.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Query("DELETE FROM history")
    fun deleteAll()

    @Insert
    fun insertHistory(history: History)

    @Delete
    fun deleteHistory(history: History)

    @Update
    fun updateHistory(history: History)

}