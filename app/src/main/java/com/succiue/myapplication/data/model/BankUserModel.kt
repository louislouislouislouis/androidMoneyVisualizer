package com.succiue.myapplication.data.model

data class BankUserModel(
    val bankToken: String,
    var accounts: List<AccountModel> = listOf()
)
