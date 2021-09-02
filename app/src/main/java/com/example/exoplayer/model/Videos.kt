package com.example.exoplayer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Videos")
data class Videos(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id:Long,
    @ColumnInfo(name = "name")
    var description: String,
    @ColumnInfo(name = "sources")
    var sources: String,
    @ColumnInfo(name = "subtitle")
    var subtitle:String,
    @ColumnInfo(name = "thumb")
    val thumb:String,
    @ColumnInfo(name = "title")
    val title:String,
)