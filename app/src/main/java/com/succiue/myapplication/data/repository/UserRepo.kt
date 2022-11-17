package com.succiue.myapplication.data.repository

import com.succiue.myapplication.data.model.UserModel
import com.succiue.myapplication.data.sources.UserSource


interface UserRepository {
    suspend fun getUser(): UserModel
}

class DefaultUserRepository(private val userSource: UserSource) : UserRepository {

    override suspend fun getUser(): UserModel {
        return userSource.getUser()
    }
}