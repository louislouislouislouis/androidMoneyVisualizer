package com.succiue.myapplication.data.repository

import com.succiue.myapplication.data.model.BankCredentialsModel
import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.data.sources.BankOnlineSource


interface BankRepository {
    suspend fun getBankAccessToken(bankCred: BankCredentialsModel): BankCredentialsModel
    val kichtaUser: KichtaUserModel
}

class DefaultBankRepo(
    kichtaUser: KichtaUserModel,
) : BankRepository {

    private val bankSource = BankOnlineSource(kichtaUser)
    override val kichtaUser: KichtaUserModel = kichtaUser

    override suspend fun getBankAccessToken(bankCred: BankCredentialsModel): BankCredentialsModel {
        return bankSource.getBankAccessToken(bankCred)
    }
}
