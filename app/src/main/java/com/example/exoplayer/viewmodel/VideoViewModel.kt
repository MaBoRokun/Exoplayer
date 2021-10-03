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
import kotlin.system.measureTimeMillis

class VideoViewModel constructor(private val repository: VideoRepository):ViewModel() {

    var videoList = MutableLiveData<List<Video>>()

    fun getVideos(){
         val time = measureTimeMillis {
             viewModelScope.launch {
                 withContext(Dispatchers.IO) {
                     val response = repository.getVideos()
                     response.enqueue(object : Callback<RetrofitResponse> {
                         override fun onResponse(
                             call: Call<RetrofitResponse>,
                             response: Response<RetrofitResponse>
                         ) {
                             if (response.code() == 200) {
                                 val result = response.body()?.categoryResponses
                                 result?.forEach {
                                     videoList.postValue(it.videos)
                                 }
                             }
                         }

                         override fun onFailure(call: Call<RetrofitResponse>, t: Throwable) {

                         }

                     })
                 }
             }
         }
         Log.d("RetrofitReq","$time ms")
    }

}