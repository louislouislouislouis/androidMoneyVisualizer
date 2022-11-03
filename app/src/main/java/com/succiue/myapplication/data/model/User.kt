package com.succiue.myapplication.data.model

import java.io.Serializable

class User(
    private var id: String,
    val displayName: String,
    private val email: String,
    var idToken: String, // trailing comma
) : Serializable {
    //TODO: Get
    val accounts = mutableListOf<Account>()
    fun verifyAccount() {

    }

    override fun toString(): String {
        return "I am the MF Class User with: $id $displayName $email $idToken"
    }

}