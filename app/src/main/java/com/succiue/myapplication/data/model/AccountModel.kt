package com.succiue.myapplication.data.model

data class BalanceModel(
    val available: Double?,
    val current: Double?,
    val iso_currency_code: String?,
    val limit: Double?,
    val unofficial_currency_code: String?
)

data class AccountModel(
    val account_id: String,
    val balances: BalanceModel,
    val mask: String,
    val name: String,
    val official_name: String,
    val subtype: String,
    val type: String,
)