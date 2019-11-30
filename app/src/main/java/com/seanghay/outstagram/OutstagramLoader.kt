package com.seanghay.outstagram

import android.app.Application
import android.content.Context

class OutstagramLoader : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        @Volatile
        lateinit var appContext: Context
    }
}