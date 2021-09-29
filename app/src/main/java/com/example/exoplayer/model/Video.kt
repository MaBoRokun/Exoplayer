package com.example.exoplayer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(tableName = "Video")
data class Video(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id:Long,
    @ColumnInfo(name = "description")
    val description:String,
    @ColumnInfo(name = "sources")
    val sources:List<String>,
    @ColumnInfo(name = "subtitle")
    val subtitle:String,
    @ColumnInfo(name = "thumb")
    val thumb:String,
    @ColumnInfo(name = "title")
    val title:String
): Serializable