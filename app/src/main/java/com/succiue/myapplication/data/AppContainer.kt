package com.succiue.myapplication.data

import AccountOnlineSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.succiue.myapplication.data.repository.*
import com.succiue.myapplication.data.sources.UserOnlineSource
import com.succiue.myapplication.data.sources.UserSource
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


interface AppContainer {
    val accountSource: AccountSource
    val accountRepository: AccountRepository
    val userSource: UserSource
    val userRepository: UserRepository
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

    //override val accountSource: AccountSource

    override val accountSource: AccountSource by lazy {
        AccountOnlineSource
    }
    override val accountRepository: AccountRepository by lazy {
        DefaultAccountRepository(accountSource)
    }

    override val userSource: UserSource by lazy {
        UserOnlineSource
    }
    override val userRepository: UserRepository by lazy {
        DefaultUserRepository(userSource)
    }

}