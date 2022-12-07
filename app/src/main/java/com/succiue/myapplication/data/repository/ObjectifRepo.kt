package com.succiue.myapplication.data.repository

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.data.model.Objectif
import com.succiue.myapplication.data.sources.ObjectifDBSource
import com.succiue.myapplication.data.sources.ObjectifOnlineSource
import com.succiue.myapplication.data.sources.ObjectifSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface ObjectifRepository {

    suspend fun getObjectifs(): kotlinx.coroutines.flow.Flow<List<Objectif>>
    suspend fun put(list: List<Objectif>)
}

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        Log.d("WORKER", "DUMMY CALL TO API")
        val mywork = OneTimeWorkRequest.Builder(RefreshDataWorker::class.java)
            .setInitialDelay(5, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(mywork)
        return Result.success()
    }

    companion object {
        const val WORK_NAME = "com.succiue.myapplication.data.sources.RefreshDataWorker"
    }
}

class DefaultObjectifRepository @Inject constructor(kichtaUser: KichtaUserModel) :
    ObjectifRepository {
    private var user = kichtaUser
    private var objectifSourceOffline: ObjectifSource = ObjectifDBSource()
    private var objectifSourceOnline: ObjectifSource = ObjectifOnlineSource(kichtaUser)

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    init {
        delayedInit(user)
    }

    private fun delayedInit(user: KichtaUserModel) {
        applicationScope.launch {
            setupRecurringWork(user)
        }
    }

    fun setupRecurringWork(user: KichtaUserModel) {


        Log.d("WORKER", "Periodic Work request for sync is scheduled")
        val repeatingRequest =
            OneTimeWorkRequest.Builder(RefreshDataWorker::class.java)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build()

        Log.d("WORKER", "Periodic Work request for sync is scheduled")
        WorkManager.getInstance().enqueue(repeatingRequest)
    }

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
