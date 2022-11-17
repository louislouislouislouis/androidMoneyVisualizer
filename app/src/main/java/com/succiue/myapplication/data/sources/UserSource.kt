package com.succiue.myapplication.data.sources

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.succiue.myapplication.data.model.BankUserModel
import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.utils.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST

interface UserSource {
    suspend fun getUser(): BankUserModel
}

class UserOnlineSource(user: KichtaUserModel) : UserSource {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(
            Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header(
                    "Authorization",
                    "Bea ${user.idToken}"
                    //"Bea eyJhbGciOiJSUzI1NiIsImtpZCI6ImY4MDljZmYxMTZlNWJhNzQwNzQ1YmZlZGE1OGUxNmU4MmYzZmQ4MDUiLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiTG91aXMgTE9NQkFSRCIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BTG01d3UxZkVxWVdRM05VSnlrMVdTU1Jqdm9HQUpkM3FIWWhvQnRpMnhVRmZRPXM5Ni1jIiwiaXNzIjoiaHR0cHM6Ly9zZWN1cmV0b2tlbi5nb29nbGUuY29tL2tpY2h0YXZpenVhbGl6ZXIiLCJhdWQiOiJraWNodGF2aXp1YWxpemVyIiwiYXV0aF90aW1lIjoxNjY4NTcyNzM0LCJ1c2VyX2lkIjoiOTlkUlVsNkdzVlN2eG5yTVZxT0c1aFRDc3YyMyIsInN1YiI6Ijk5ZFJVbDZHc1ZTdnhuck1WcU9HNWhUQ3N2MjMiLCJpYXQiOjE2Njg2NTIzOTUsImV4cCI6MTY2ODY1NTk5NSwiZW1haWwiOiJsb3Vpcy5yYXBoYWVsLmxvbWJhcmRAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZ29vZ2xlLmNvbSI6WyIxMDc4OTMxNzA3NDU3NzI4MTk4MjMiXSwiZW1haWwiOlsibG91aXMucmFwaGFlbC5sb21iYXJkQGdtYWlsLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6Imdvb2dsZS5jb20ifX0.SrQmX_9cPvp_wGL5Byd0xZKKiW-eQflVsupUrh9Dr6BJRk5Mr0atF28-7qOZekS4cQDMRd0AUtNmsgHDHeHAwgPDWA9FY1LKmyjfSxMssJJAKkdWykIZR_DgqPfC5TXuZkgq9PNFbzlkyLvqywPqK1j7ibhCZnWehsxmdWoEFqQUUj3WbJOzLeZGbUfiqGAXKRjefgIGLM8Yp8MwIaDqym9FhZHUzEoHSY9w48q0qJc25UHOzsYntXajqqS22n45L08YhzCxHw0nCOtBrnQ-_yW54a-1tApnE2V_drHgmMvDJjHqbaIZ6A80qAq4c_06B42m5LdhpXlbLtAMDjXvTA"
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


    data class BankAccessToken(
        val accessToken: String
    )

    interface SteamAppsService {
        @POST("bank/getAccessToken")
        suspend fun getBankAccessToken(): BankAccessToken
    }

    private val retrofitService: SteamAppsService by lazy {
        retrofit.create(SteamAppsService::class.java)
    }

    override suspend fun getUser(): BankUserModel {
        val token = retrofitService.getBankAccessToken().accessToken
        Log.d("UserSource", "BankAccessToken: ${retrofitService.getBankAccessToken().accessToken}")
        return BankUserModel(token, listOf())
    }
}
