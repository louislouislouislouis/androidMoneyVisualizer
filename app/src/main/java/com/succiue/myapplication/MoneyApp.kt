package com.succiue.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoneyApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}