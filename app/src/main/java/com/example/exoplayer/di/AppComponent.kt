package com.example.exoplayer.di

import com.example.exoplayer.network.RetrofitService
import com.example.exoplayer.viewmodel.RoomViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RetrofitService::class])
interface AppComponent {

    fun inject(myApp: MyApp)
    fun inject(roomViewModel: RoomViewModel)

}