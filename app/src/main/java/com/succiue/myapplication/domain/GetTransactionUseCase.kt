package com.succiue.myapplication.domain

import com.succiue.myapplication.data.repository.BankRepository
import com.succiue.myapplication.data.repository.UserRepository
import com.succiue.myapplication.ui.viewmodels.MainBasicUiState
import com.succiue.myapplication.ui.viewmodels.TransactionUiState
import com.succiue.myapplication.utils.multipleNonNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class GetTransactionUseCase(
    private val userRepo: UserRepository,
    private val bankrepo: BankRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun getBalance(uiState: MainBasicUiState): MainBasicUiState =
        withContext(defaultDispatcher) {
            val accountInfo = userRepo.getBalance()
            var totalAmount: Double = 0.0
            var currency: Currency? = null

            accountInfo.forEach { accountMdl ->
                currency?.let { curr ->
                    if (Currency.getInstance(accountMdl.balances.iso_currency_code) == curr) {
                        totalAmount += accountMdl.balances.available!!
                    }
                } ?: run {
                    currency = Currency.getInstance(accountMdl.balances.iso_currency_code)
                }
            }

            var totalString = ""
            var currencyString = ""
            currency?.let { curr ->
                currencyString = curr.symbol.toString()
                totalString = String.format("%.2f", totalAmount)
            }
            return@withContext uiState.copy(totalAmount = totalString, currency = currencyString);
        }

    suspend fun getTransaction(uiState: MainBasicUiState): MainBasicUiState =
        withContext(defaultDispatcher) {
            val accountTR = userRepo.getTransactions()
            var listForUI: ArrayList<TransactionUiState> = ArrayList()
            val formatters = DateTimeFormatter.ofPattern("d/MM/uuuu")
            accountTR.forEach { trMdl ->
                if (listForUI.size == 100) return@forEach
                multipleNonNull(
                    trMdl.iso_currency_code,
                    trMdl.category,
                    trMdl.authorized_date
                ) { iso, cat, date ->
                    var date = LocalDate.parse(date).format(formatters)
                    listForUI.add(
                        TransactionUiState(
                            amount = String.format(
                                "%.2f",
                                -1 * trMdl.amount
                            ) + Currency.getInstance(iso).symbol.toString(),
                            merchant = trMdl.merchant_name?.let { it } ?: "Unknown",
                            category = cat.first(),
                            date = date
                        )
                    )
                }
            }
            return@withContext uiState.copy(transactionList = listForUI.toList())

        }
}