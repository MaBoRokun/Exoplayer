package com.example.exoplayer.repository

import com.example.exoplayer.network.RetrofitService

class VideoRepository constructor(private val retrofitService: RetrofitService) {
    fun getVideos() = retrofitService.VideoService.fetchData()
}