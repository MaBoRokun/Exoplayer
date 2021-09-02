package com.example.exoplayer.DAO

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.exoplayer.model.Videos


@Database(entities = [Videos::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoDAO(): VideoDao?
}