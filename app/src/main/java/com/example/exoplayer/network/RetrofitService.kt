package com.example.exoplayer.network

import com.example.exoplayer.resource.VideoCredentials
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object RetrofitService {
    private var mClient: OkHttpClient? = null
    @Singleton
    @Provides
    fun getClient():OkHttpClient{
        if (mClient == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val httpBuilder = OkHttpClient.Builder()
            httpBuilder
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
            mClient = httpBuilder.build()

        }
        return mClient!!
    }
//    private val client: OkHttpClient
//        get() {
//            if (mClient == null) {
//                val interceptor = HttpLoggingInterceptor()
//                interceptor.level = HttpLoggingInterceptor.Level.BODY
//
//                val httpBuilder = OkHttpClient.Builder()
//                httpBuilder
//                    .connectTimeout(15, TimeUnit.SECONDS)
//                    .readTimeout(20, TimeUnit.SECONDS)
//                    .addInterceptor(interceptor)
//                mClient = httpBuilder.build()
//
//            }
//            return mClient!!
//        }
    @Singleton
    @Provides
     fun retrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(VideoCredentials.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build()
    }
    @Singleton
    val VideoService: VideoAPI by lazy {
        retrofit().create(VideoAPI::class.java)
    }
}