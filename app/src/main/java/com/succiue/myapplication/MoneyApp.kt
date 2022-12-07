package com.succiue.myapplication

import android.app.Application
import android.content.Context
import com.succiue.myapplication.data.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoneyApp : Application() {

    companion object {
        private var sApplication: Application? = null

        fun getApplication(): Application? {
            return sApplication
        }

        fun getContext(): Context? {
            return getApplication()!!.applicationContext
        }
    }


    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    override fun onCreate() {
        super.onCreate()
        sApplication = this

    }
}