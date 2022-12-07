package com.succiue.myapplication.data.repository

import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.data.model.Objectif
import com.succiue.myapplication.data.sources.ObjectifDBSource
import com.succiue.myapplication.data.sources.ObjectifOnlineSource
import com.succiue.myapplication.data.sources.ObjectifSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

interface ObjectifRepository {

    suspend fun getObjectifs(): kotlinx.coroutines.flow.Flow<List<Objectif>>
    suspend fun put(list: List<Objectif>)
}

class DefaultObjectifRepository @Inject constructor(kichtaUser: KichtaUserModel) :
    ObjectifRepository {
    private var objectifSourceOffline: ObjectifSource = ObjectifDBSource()
    private var objectifSourceOnline: ObjectifSource = ObjectifOnlineSource(kichtaUser)

    override suspend fun put(list: List<Objectif>) {
        objectifSourceOnline.putObjectif(flowOf(list))
        objectifSourceOffline.putObjectif(flowOf(list))
    }

    override suspend fun getObjectifs(): Flow<List<Objectif>> {
        var test = objectifSourceOnline.getObjectifs()
        objectifSourceOffline.putObjectif(test)
        return test
    }
}
