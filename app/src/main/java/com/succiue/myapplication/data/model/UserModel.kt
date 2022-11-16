package com.succiue.myapplication.data.model

import java.io.Serializable

data class UserModel(
    private var id: String,
    val displayName: String,
    private val email: String,
    var idToken: String, // trailing comma
) : Serializable {
    //TODO: Get

    val accounts = mutableListOf<AccountModel>()


}