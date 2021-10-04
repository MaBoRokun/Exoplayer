package com.example.exoplayer.repository

import com.example.exoplayer.network.RetrofitService
import javax.inject.Inject

class VideoRepository
@Inject
constructor(private val retrofitService: RetrofitService) {
    suspend fun getVideos() = retrofitService.VideoService.fetchData()
}