package com.succiue.myapplication.data.repository

import com.succiue.myapplication.data.model.BankCredentialsModel
import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.data.sources.BankOnlineSource


interface BankRepository {
    suspend fun getBankLinkToken(): BankCredentialsModel
    suspend fun getBankAccessToken(bankCred: BankCredentialsModel): BankCredentialsModel
    val kichtaUser: KichtaUserModel
}

class DefaultBankRepo(
    override val kichtaUser: KichtaUserModel,
) : BankRepository {

    private val bankSource = BankOnlineSource(kichtaUser)

    override suspend fun getBankAccessToken(bankCred: BankCredentialsModel): BankCredentialsModel {
        return bankSource.getBankAccessToken(bankCred)
    }

    override suspend fun getBankLinkToken(): BankCredentialsModel {
        return bankSource.getBankLinkToken()
    }
}
