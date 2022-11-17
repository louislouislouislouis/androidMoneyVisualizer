package com.succiue.myapplication.data.repository

import com.succiue.myapplication.data.model.AccountModel
import com.succiue.myapplication.data.model.BankUserModel
import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.data.sources.UserOnlineSource


interface UserRepository {
    val kichtaUser: KichtaUserModel
    suspend fun getUser(): BankUserModel
    suspend fun getBalance(): List<AccountModel>
}

class DefaultUserRepository(
    kichtaUser: KichtaUserModel,
) : UserRepository {

    private val userSource = UserOnlineSource(kichtaUser)
    override val kichtaUser: KichtaUserModel = kichtaUser

    override suspend fun getUser(): BankUserModel {
        return userSource.getUser()
    }

    override suspend fun getBalance(): List<AccountModel> {
        return userSource.getBalance()
    }


}