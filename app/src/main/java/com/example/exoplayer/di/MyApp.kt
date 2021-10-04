package com.example.exoplayer.di

import android.app.Application

class MyApp : Application() {
    companion object {
        lateinit var instance: MyApp
            private set
    }

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        initComponent()
    }

    private fun initComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
        appComponent.inject(this)
    }

    fun getAppComponent(): AppComponent = appComponent
}