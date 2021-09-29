package com.example.exoplayer.DAO

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.exoplayer.model.Video
import com.example.exoplayer.model.VideoSourceConverter


@Database(entities = arrayOf(Video::class), version = 1, exportSchema = false)
@TypeConverters(VideoSourceConverter::class)
abstract class VideoDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

    companion object {

        @Volatile
        private var INSTANCE: VideoDatabase? = null

        fun getDatabaseClient(context: Context): VideoDatabase {

            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {

                INSTANCE = Room
                    .databaseBuilder(context, VideoDatabase::class.java, "LOGIN_DATABASE")
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!

            }
        }

    }
}
