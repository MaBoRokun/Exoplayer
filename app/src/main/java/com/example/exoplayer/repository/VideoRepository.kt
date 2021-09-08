package com.example.exoplayer.repository

import com.example.exoplayer.Resource

class VideoRepository constructor(private val resource: Resource) {

   fun getVideos() = resource.createDataSet()

}