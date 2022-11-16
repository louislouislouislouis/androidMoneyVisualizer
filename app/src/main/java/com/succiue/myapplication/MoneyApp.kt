package com.succiue.myapplication

import android.app.Application
import com.succiue.myapplication.data.AppContainer
import com.succiue.myapplication.data.DefaultAppContainer

class MoneyApp : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}