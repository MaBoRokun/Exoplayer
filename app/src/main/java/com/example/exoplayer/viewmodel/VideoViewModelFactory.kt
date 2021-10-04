package com.example.exoplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.exoplayer.repository.VideoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoViewModelFactory
@Inject
constructor(private val repository: VideoRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(VideoViewModel::class.java)) {
            VideoViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}