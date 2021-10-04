package com.example.exoplayer.di

import android.app.Application
import android.content.Context
import com.example.exoplayer.database.VideoDao
import com.example.exoplayer.database.VideoDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Singleton
    @Provides
    fun getVideoDao(videoDatabase: VideoDatabase): VideoDao = videoDatabase.videoDao()

    @Singleton
    @Provides
    fun getRoomObjIntance(): VideoDatabase = VideoDatabase.getDatabaseInstance(provideAppContext())


    @Singleton
    @Provides
    fun provideAppContext(): Context = application.applicationContext
}