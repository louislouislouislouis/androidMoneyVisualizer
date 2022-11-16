package com.succiue.myapplication.data.sources

import com.squareup.moshi.Json
import com.succiue.myapplication.data.model.UserModel
import retrofit2.Retrofit
import retrofit2.http.GET

interface UserSource {
    suspend fun getUser(): UserModel
}

object UserOnlineSource : UserSource {
    private const val BASE_URL = "http://api.steampowered.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .build()

    data class SteamAppList(
        @Json(name = "user")
        val user: User
    )

    data class User(
        @Json(name = "user")
        val user: UserModel
    )

    interface SteamAppsService {
        @GET("ISteamApps/GetAppList/v0002?format=json")
        suspend fun GetUser(): User
    }

    private val retrofitService: SteamAppsService by lazy {
        retrofit.create(SteamAppsService::class.java)
    }

    override suspend fun getUser(): UserModel {
        return retrofitService.GetUser().user
    }
}
