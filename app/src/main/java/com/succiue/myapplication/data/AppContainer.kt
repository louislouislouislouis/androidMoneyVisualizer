package com.succiue.myapplication.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.succiue.myapplication.data.repository.AccountRepository
import com.succiue.myapplication.data.repository.AccountSource
import com.succiue.myapplication.data.repository.DefaultAccountRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


interface AppContainer {
    val accountSource: AccountSource
    val accountRepository: AccountRepository
}

class DefaultAppContainer : AppContainer {
    private companion object {
        private const val BASE_URL = "http://api.steampowered.com"

        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        private val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
    }

    override val accountSource: AccountSource
        get() = TODO("Not yet implemented")

    override val accountRepository: AccountRepository by lazy {
        DefaultAccountRepository(accountSource)
    }
}