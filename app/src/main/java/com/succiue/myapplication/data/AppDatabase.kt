package com.succiue.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.succiue.myapplication.MoneyApp
import com.succiue.myapplication.data.model.Objectif
import com.succiue.myapplication.data.sources.ObjectifDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [Objectif::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun objectifDao(): ObjectifDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "succieu_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }
}

@InstallIn(SingletonComponent::class)
@Module
object AppDatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(): AppDatabase {
        return AppDatabase.getDatabase(MoneyApp.getContext()!!)
    }
}