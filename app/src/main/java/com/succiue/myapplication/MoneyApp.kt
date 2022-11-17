package com.succiue.myapplication

import android.app.Application
import com.succiue.myapplication.data.DefaultAppContainer

class MoneyApp : Application() {
    lateinit var container: DefaultAppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}