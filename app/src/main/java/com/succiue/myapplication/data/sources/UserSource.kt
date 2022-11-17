package com.succiue.myapplication.data.sources

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.succiue.myapplication.data.model.AccountModel
import com.succiue.myapplication.data.model.BankUserModel
import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.data.model.TransactionModel
import com.succiue.myapplication.utils.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST

interface UserSource {
    suspend fun getUser(): BankUserModel
    suspend fun getBalance(): List<AccountModel>
    suspend fun getTransactions(): List<TransactionModel>

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

    data class Accounts(
        val accounts: List<AccountModel>
    )


    data class Transactions(
        val transactions: List<TransactionModel>,
        val accounts: List<AccountModel>,
        val total_transactions: Long
    )

    data class DataFromBank(
        val data: Accounts
    )


    data class DataFromBank2(
        val data: Transactions
    )

    interface BankAccessServices {
        @POST("bank/getAccessToken")
        suspend fun getBankAccessToken(): BankAccessToken

        @POST("bank/getBalanceInfo")
        suspend fun getBalance(): DataFromBank

        @POST("bank/getTransactionInfo")
        suspend fun getTransactions(): DataFromBank2
    }

    private val retrofitService: BankAccessServices by lazy {
        retrofit.create(BankAccessServices::class.java)
    }

    override suspend fun getUser(): BankUserModel {
        val token = retrofitService.getBankAccessToken().accessToken
        return BankUserModel(token, listOf())
    }

    override suspend fun getBalance(): List<AccountModel> {
        return retrofitService.getBalance().data.accounts
    }

    override suspend fun getTransactions(): List<TransactionModel> {
        return retrofitService.getTransactions().data.transactions
    }
}
