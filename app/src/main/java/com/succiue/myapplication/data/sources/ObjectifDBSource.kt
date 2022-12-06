package com.succiue.myapplication.data.sources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.succiue.myapplication.MoneyApp
import com.succiue.myapplication.data.AppDatabase
import com.succiue.myapplication.data.model.Objectif
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjectifDao {
    @Query("SELECT * FROM objectif")
    fun getAll(): Flow<List<Objectif>>

    @Query("SELECT * FROM objectif WHERE userId = :userId")
    fun getById(userId: String): Flow<Objectif>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(objectif: List<Objectif>)
}

interface ObjectifSource {
    suspend fun getObjectifs(): Flow<List<Objectif>>
}

object ObjectifDBSource : ObjectifSource {

    var appDatabase: AppDatabase

    init {
        val appContext = MoneyApp.getContext()
        val utilitiesEntryPoint =
            appContext?.let {
                EntryPointAccessors.fromApplication(
                    it, DefaultCacheObjectifSourceEntryPoint::class.java
                )
            }
        appDatabase = utilitiesEntryPoint?.appDatabase!!
    }


    override suspend fun getObjectifs(): Flow<List<Objectif>> {
        //refreshObjectifs()
        return appDatabase.objectifDao().getAll()
    }

}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DefaultCacheObjectifSourceEntryPoint {
    var appDatabase: AppDatabase
}