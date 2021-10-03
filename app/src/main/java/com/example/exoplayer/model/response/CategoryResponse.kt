package com.example.exoplayer.model.response

import com.example.exoplayer.model.Video
import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("videos")
    val videos: MutableList<Video>
)
