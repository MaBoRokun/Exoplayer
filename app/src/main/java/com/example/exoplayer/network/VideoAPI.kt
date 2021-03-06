package com.example.exoplayer.network

import com.example.exoplayer.model.response.RetrofitResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface VideoAPI {
    @GET("/DavidStdn/NitrixTestTask/main/videos")
   suspend fun fetchData(): Response<RetrofitResponse>
}
