package com.example.exoplayer.DAO

import androidx.room.*
import com.example.exoplayer.model.Videos


@Dao
interface VideoDao {

    @Query("SELECT * FROM Videos")
    fun getAll(): List<Videos>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(video: Videos?)

    @Update
    fun update(video: Videos?)

    @Delete
    fun delete(video: Videos?)
}