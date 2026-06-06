package com.example.chambape

import android.app.Application
import com.example.chambape.di.AppModule

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppModule.init(this)
    }
}