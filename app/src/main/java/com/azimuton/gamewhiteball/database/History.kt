package com.azimuton.gamewhiteball.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
class History(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "count")val count: String,
    @ColumnInfo(name = "color")val color: String,
    @ColumnInfo(name = "time")val time: String
)