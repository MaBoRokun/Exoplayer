package com.example.exoplayer.database

import androidx.room.*
import com.example.exoplayer.model.Video
import dagger.Provides


@Dao
interface VideoDao {

    @Query("SELECT * FROM Video")
    fun getAllFromDB(): List<Video>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(video: Video)

    @Update
    fun update(video: Video)

    @Delete
    fun delete(video: Video)
}
