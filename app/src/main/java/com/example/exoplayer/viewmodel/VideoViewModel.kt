package com.example.exoplayer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.exoplayer.model.Videos
import com.example.exoplayer.repository.VideoRepository

class VideoViewModel
constructor (private val repository: VideoRepository) :ViewModel() {
    var videoList = MutableLiveData<List<Videos>>()

    fun getVideos():MutableList<Videos>{
      return repository.getVideos()
    }
}