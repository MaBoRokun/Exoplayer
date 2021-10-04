package com.example.exoplayer.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exoplayer.model.Video
import com.example.exoplayer.model.response.RetrofitResponse
import com.example.exoplayer.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.system.measureTimeMillis

class VideoViewModel
@Inject
constructor(private val repository: VideoRepository):ViewModel() {

    var videoList = MutableLiveData<List<Video>>()

    fun getVideos(){
         val time = measureTimeMillis {
             viewModelScope.launch {
                 withContext(Dispatchers.IO) {
                     val response = repository.getVideos()
                     if(response.isSuccessful){
                         val result = response.body()?.categoryResponses
                         result?.forEach {
                             videoList.postValue(it.videos)
                         }
                     }
                 }
             }
         }
         Log.d("RetrofitReq","$time ms")
    }

}