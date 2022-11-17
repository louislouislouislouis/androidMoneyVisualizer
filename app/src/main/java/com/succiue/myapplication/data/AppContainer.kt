package com.succiue.myapplication.data

import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.data.repository.BankRepository
import com.succiue.myapplication.data.repository.DefaultBankRepo
import com.succiue.myapplication.data.repository.DefaultUserRepository
import com.succiue.myapplication.data.repository.UserRepository


interface AppContainer {
    var userRepository: UserRepository?
    val bankRepository: BankRepository?
}

class DefaultAppContainer() : AppContainer {

    override var userRepository: UserRepository? = null
    override var bankRepository: BankRepository? = null

    fun setUser(user: KichtaUserModel) {
        userRepository = DefaultUserRepository(user)
        bankRepository = DefaultBankRepo(user)
    }

}