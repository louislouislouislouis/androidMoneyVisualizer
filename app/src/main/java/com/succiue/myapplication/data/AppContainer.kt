package com.succiue.myapplication.data

import AccountOnlineSource
import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.data.repository.*


interface AppContainer {
    val accountSource: AccountSource
    val accountRepository: AccountRepository

    //val userSource: UserSource
    var userRepository: UserRepository?
    val bankRepository: BankRepository?
}

class DefaultAppContainer() : AppContainer {

    override val accountSource: AccountSource by lazy {
        AccountOnlineSource
    }
    override val accountRepository: AccountRepository by lazy {
        DefaultAccountRepository(accountSource)
    }

    override var userRepository: UserRepository? = null
    override var bankRepository: BankRepository? = null

    fun setUser(user: KichtaUserModel) {
        userRepository = DefaultUserRepository(user)
        bankRepository = DefaultBankRepo(user)
    }

}