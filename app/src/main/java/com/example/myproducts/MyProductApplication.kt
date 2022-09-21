package com.example.myproducts

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyProductApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MyProductApplication
    }
}