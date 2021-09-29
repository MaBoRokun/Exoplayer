package com.example.exoplayer.DAO

import androidx.room.*
import com.example.exoplayer.model.Video


@Dao
interface VideoDao {

    @Query("SELECT * FROM Video")
    fun getAll(): List<Video>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(video: Video)

    @Update
    fun update(video: Video)

    @Delete
    fun delete(video: Video)
}