package com.example.exoplayer.model

import androidx.room.TypeConverter
import java.util.*
import java.util.stream.Collectors

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
