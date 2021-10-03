package com.example.exoplayer.di

import android.app.Application
import android.content.Context
import com.example.exoplayer.database.VideoDao
import com.example.exoplayer.database.VideoDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: Application){

    @Singleton
    @Provides
    fun getVideoDao(videoDatabase: VideoDatabase):VideoDao{
        return videoDatabase.videoDao()
    }

    @Singleton
    @Provides
    fun getRoomObjIntance():VideoDatabase{
        return VideoDatabase.getDatabaseInstance(provideAppContext())
    }

    @Singleton
    @Provides
    fun provideAppContext():Context{
        return application.applicationContext
    }
}