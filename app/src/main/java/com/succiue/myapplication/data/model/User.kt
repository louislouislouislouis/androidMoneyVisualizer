package com.succiue.myapplication.data.model

import java.io.Serializable

class User(
    private var id: String,
    private val displayName: String,
    private val email: String,
    private var idToken: String, // trailing comma
) : Serializable {
    override fun toString(): String {
        return "I am the MF Class User with: $id $displayName $email $idToken"
    }

}