package com.succiue.myapplication.data.model

data class BalanceModel(
    val available: Long?,
    val current: Long?,
    val iso_currency_code: String?,
    val limit: Long?,
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