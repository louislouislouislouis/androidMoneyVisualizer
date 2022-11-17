package com.succiue.myapplication.data.model

data class Location(
    val address: String?,
    val city: String?,
    val country: String?,
    val lat: String?,
    val lon: String?,
    val postal_code: String?,
    val region: String?,
    val store_number: String?,
)

data class PaymentMeta(
    val by_order_of: String?,
    val payee: String?,
    val payer: String?,
    val payment_method: String?,
    val payment_processor: String?,
    val ppd_id: String?,
    val reason: String?,
    val reference_number: String?,
)

data class TransactionModel(
    val account_id: String?,
    val account_owner: String?,
    val amount: Double,
    val authorized_date: String?,
    val authorized_datetime: String?,
    val category: List<String>,
    val category_id: String?,
    val check_number: String?,
    val date: String?,
    val datetime: String?,
    val iso_currency_code: String?,
    val location: Location?,
    val merchant_name: String?,
    val name: String?,
    val payment_channel: String?,
    val payment_meta: PaymentMeta?,
    val pending: Boolean?,
    val pending_transaction_id: String?,
    val personal_finance_category: String?,
    val transaction_code: String?,
    val transaction_id: String?,
    val transaction_type: String?,
    val unofficial_currency_code: String?,
)