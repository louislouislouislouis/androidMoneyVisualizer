package com.succiue.myapplication.data.model

import java.io.Serializable

data class KichtaUserModel(
    private val idKichta: String,
    val idToken: String,
    private val email: String,
    val displayName: String,
    private var connectedBankUserModel: BankUserModel? = null
) : Serializable
