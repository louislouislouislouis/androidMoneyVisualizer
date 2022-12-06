package com.succiue.myapplication.domain

import com.succiue.myapplication.data.model.BankCredentialsModel
import com.succiue.myapplication.data.model.BankUserModel
import com.succiue.myapplication.data.repository.BankRepository
import com.succiue.myapplication.data.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetUserUseCase(
    private val userRepo: UserRepository,
    private val bankrepo: BankRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun getUser(): BankUserModel =
        withContext(defaultDispatcher) {
            userRepo.getUser()
        }

    suspend fun getBankAccessToken(credentialsBank: BankCredentialsModel): BankCredentialsModel =
        withContext(defaultDispatcher) {
            bankrepo.getBankAccessToken(credentialsBank)
        }

    suspend fun getBankLinkToken(): BankCredentialsModel =
        withContext(defaultDispatcher) {
            bankrepo.getBankLinkToken()
        }

}