package com.example.exercise.ui

import android.app.Application
import android.content.Context
import com.example.exercise.dagger2.AppComponent
import com.example.exercise.dagger2.DaggerAppComponent
import com.example.exercise.dagger2.RandomRepositoryModule

class MainApplication : Application() {
    lateinit var randomJokeRepositoryComponent: AppComponent
    init {
        instance = this
    }

    companion object {
        private var instance: MainApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
   
    override fun onCreate() {
        super.onCreate()
        randomJokeRepositoryComponent = initDagger()
    }

    private fun initDagger(): AppComponent =
        DaggerAppComponent.builder()
            .randomRepositoryModule(RandomRepositoryModule())
            .build()
    
    
}