package com.example.exoplayer.di

import com.example.exoplayer.viewmodel.RoomViewModel
import com.example.exoplayer.viewmodel.VideoViewModel
import dagger.Component
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(roomViewModel: RoomViewModel)

}