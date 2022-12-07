package com.succiue.myapplication.data.sources

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.succiue.myapplication.MoneyApp
import com.succiue.myapplication.data.AppDatabase
import com.succiue.myapplication.data.model.*
import com.succiue.myapplication.utils.Constant
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import java.text.SimpleDateFormat
import java.util.*


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
    suspend fun putObjectif(objectifs: Flow<List<Objectif>>)
}

class ObjectifOnlineSource(user: KichtaUserModel) : ObjectifSource {


    // Your custom date adapter here
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(CustomDateAdapter())
        .build()

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(
            Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header(
                    "Authorization",
                    "Bea ${user.idToken}"
                )
                builder.header("X-Platform", "Android")
                return@Interceptor chain.proceed(builder.build())
            }
        )
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constant.URL_BANK)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()

    data class DataFromBank2(
        val objectives: List<Objectif>
    )

    interface ObjectifAccessServices {
        @GET("bank/objectif")
        suspend fun getObjectives(): DataFromBank2

        @POST("bank/objectif")
        suspend fun postObjectif(@Body objectif: Objectif)

        @DELETE("bank/objectif")
        suspend fun getTransactions()
    }

    private val retrofitService: ObjectifAccessServices by lazy {
        retrofit.create(ObjectifAccessServices::class.java)
    }

    override suspend fun getObjectifs(): Flow<List<Objectif>> {
        val objectives = retrofitService.getObjectives().objectives
        Log.d("TEST", "getObjectifs: $objectives")
        return flowOf(objectives)
    }

    override suspend fun putObjectif(objectifs: Flow<List<Objectif>>) {
        val objectives = objectifs.first()
        objectives.forEach() {
            retrofitService.postObjectif(it)
        }
    }

}


class ObjectifDBSource() : ObjectifSource {

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
        return appDatabase.objectifDao().getAll()
    }

    override suspend fun putObjectif(objectifs: Flow<List<Objectif>>) {
        withContext(Dispatchers.IO) {
            appDatabase.objectifDao().insert(objectifs.first())
        }

    }

}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DefaultCacheObjectifSourceEntryPoint {
    var appDatabase: AppDatabase
}

class CustomDateAdapter : JsonAdapter<Date>() {
    private val dateFormat = SimpleDateFormat(SERVER_FORMAT, Locale.getDefault())


    companion object {
        const val SERVER_FORMAT = ("yyyy-MM-dd'T'HH:mm") // define your server format here
    }

    @FromJson
    override fun fromJson(reader: com.squareup.moshi.JsonReader): Date? {
        return try {
            val dateAsString = reader.nextString()
            synchronized(dateFormat) {
                dateFormat.parse(dateAsString)
            }
        } catch (e: Exception) {
            null
        }
    }

    @ToJson
    override fun toJson(writer: com.squareup.moshi.JsonWriter, value: Date?) {
        if (value != null) {
            synchronized(dateFormat) {
                writer.value(value.toString())
            }
        }
    }
}