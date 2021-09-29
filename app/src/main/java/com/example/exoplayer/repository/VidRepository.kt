package com.example.exoplayer.repository

import android.content.Context
import com.example.exoplayer.DAO.VideoDatabase
import com.example.exoplayer.network.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class VidRepository constructor(private val retrofitService: RetrofitService) {
    fun getVideos() = retrofitService.VideoService.fetchData()
}