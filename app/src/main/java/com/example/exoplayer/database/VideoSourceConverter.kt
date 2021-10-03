package com.example.exoplayer.database

import androidx.room.TypeConverter

class VideoSourceConverter {

    @TypeConverter
    fun toString(source: List<String>): String {
        return source.joinToString(",")
    }

    @TypeConverter
    fun toList(data: String): List<String> {
        return data.split(",".toRegex()).toList()
    }

}
