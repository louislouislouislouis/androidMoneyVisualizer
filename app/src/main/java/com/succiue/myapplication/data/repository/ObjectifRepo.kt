package com.succiue.myapplication.data.repository

import com.succiue.myapplication.MoneyApp
import com.succiue.myapplication.data.model.Objectif
import com.succiue.myapplication.data.sources.ObjectifDBSource
import com.succiue.myapplication.data.sources.ObjectifSource
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface ObjectifRepository {
    suspend fun getObjectifs(): kotlinx.coroutines.flow.Flow<List<Objectif>>
}

class DefaultObjectifRepository @Inject constructor() : ObjectifRepository {
    private var objectifSource: ObjectifSource

    init {

        val appContext = MoneyApp.getContext()
        val utilitiesEntryPoint =
            appContext?.let {
                EntryPointAccessors.fromApplication(
                    it, DefaultObjectifRepoEntryPoint::class.java
                )
            }
        objectifSource = utilitiesEntryPoint?.objectifSource!!
    }

    override suspend fun getObjectifs(): Flow<List<Objectif>> {
        return objectifSource.getObjectifs()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DefaultObjectifRepoEntryPoint {
    var objectifSource: ObjectifSource
}

@InstallIn(SingletonComponent::class)
@Module
object GameRepositoryModule {
    @Singleton
    @Provides
    fun provideGameRepo(): ObjectifRepository {
        return DefaultObjectifRepository()
    }
}

@InstallIn(SingletonComponent::class)
@Module
object GameSourceModule {
    @Provides
    @Singleton
    fun provideObjectifSource(): ObjectifSource {
        return ObjectifDBSource
    }
}