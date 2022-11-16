package com.succiue.myapplication.data.repository

import com.succiue.myapplication.data.model.AccountModel


interface AccountSource {
    suspend fun getGames(): List<AccountModel>
}

interface AccountRepository {
    suspend fun getSteamApps(): List<AccountModel>
}

class DefaultAccountRepository(private val AccountSource: AccountSource) : AccountRepository {
    override suspend fun getSteamApps(): List<AccountModel> {
        return AccountSource.getGames()
    }
}