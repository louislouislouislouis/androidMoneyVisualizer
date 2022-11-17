package com.succiue.myapplication.data.sources

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.succiue.myapplication.data.model.BankCredentialsModel
import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.utils.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


interface BankSource {
    suspend fun getBankLinkToken(): BankCredentialsModel
    suspend fun getBankAccessToken(cred: BankCredentialsModel): BankCredentialsModel
}

class BankOnlineSource(user: KichtaUserModel) : BankSource {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private var logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
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
            .addInterceptor(logging)
    }
        .build()


    private val retrofit = Retrofit.Builder()
        .baseUrl(Constant.URL_BANK)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()

    data class AccessToken(
        @Json(name = "access_token")
        val accessToken: String
    )

    data class TokenLink(
        @SerializedName("tokenLink") val tokenLink: String
    )

    data class PublicToken(
        @SerializedName("publicToken") val publicToken: String
    )

    interface BankTokenService {
        @POST("bank/exchangePktoAccessToken")
        suspend fun getBankAccessToken(@Body tkn: PublicToken): AccessToken

        @POST("/bank/getLink")
        suspend fun getMyBankLinkToken(): TokenLink
    }


    private val retrofitService: BankTokenService by lazy {
        retrofit.create(BankTokenService::class.java)
    }

    override suspend fun getBankAccessToken(cred: BankCredentialsModel): BankCredentialsModel {
        cred.publicToken?.let { pbTkn ->
            val accessToken = retrofitService.getBankAccessToken(PublicToken(pbTkn))
            cred.accessToken = accessToken.accessToken;
        } ?: run {
            Log.d("BankSource", "ERROR - > NO PUBLIKTOKEN")
        }
        return cred
    }

    override suspend fun getBankLinkToken(): BankCredentialsModel {
        var lktoken = retrofitService.getMyBankLinkToken()
        return BankCredentialsModel(linkToken = lktoken.tokenLink)
    }

}

