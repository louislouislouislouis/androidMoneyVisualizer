package com.succiue.myapplication.ui.viewmodels

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.linkTokenConfiguration
import com.plaid.link.result.LinkSuccess
import com.succiue.myapplication.MoneyApp
import com.succiue.myapplication.data.model.BankCredentialsModel
import com.succiue.myapplication.data.model.BankUserModel
import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.data.repository.BankRepository
import com.succiue.myapplication.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.util.*
import java.util.Currency.getInstance

data class MainBasicUiState(
    val loading: Boolean = false,
    val needAnAccess: Boolean = true,
    val name: String = "",
    val totalAmount: String = "",
    val currency: String = ""
)

data class NumberToShow(
    val TotalAccount: Double
)

class MainViewModel(
    var user: KichtaUserModel,
    private val userRepo: UserRepository,
    private val bankRepo: BankRepository
) : ViewModel() {

    fun getBalanceInfoFrom() {
        viewModelScope.launch {
            val accountInfo = userRepo.getBalance()
            var totalAmount: Double = 0.0
            var currency: Currency? = null


            accountInfo.forEach { accountMdl ->
                Log.d("AAA", accountMdl.toString())
                currency?.let { curr ->
                    if (getInstance(accountMdl.balances.iso_currency_code) == curr) {
                        totalAmount += accountMdl.balances.available!!
                    }
                } ?: run {
                    currency = getInstance(accountMdl.balances.iso_currency_code)
                }
            }
            var totalString = ""
            var currencyString = ""
            currency?.let { curr ->
                currencyString = curr.symbol.toString()
                totalString = String.format("%.2f", totalAmount)
            }
            uiState = uiState.copy(totalAmount = totalString, currency = currencyString)

            Log.d("MainViewModel", "Voila les balance$accountInfo")
        }
    }

    fun getTransactionInfoFrom() {
        viewModelScope.launch {
            val accountTR = userRepo.getTransactions()
            Log.d("MainViewModel", "Voila les balance$accountTR")
        }
    }

    var uiState by mutableStateOf(
        MainBasicUiState(
            loading = false,
            needAnAccess = true,
            name = user.displayName
        )
    )
        private set

    var credentialsBank = BankCredentialsModel()

    /**
     * The launcher has to be initialized by the activity
     */
    var linkAccountToPlaid: ActivityResultLauncher<LinkTokenConfiguration>? = null

    /**
     * Launcher callback
     */
    fun onSuccess(res: LinkSuccess) {
        // Send public_token to your server, exchange for access_token
        var publicTokenValue = res.publicToken
        credentialsBank.publicToken = publicTokenValue
        viewModelScope.launch {
            try {
                bankRepo.getBankAccessToken(credentialsBank).accessToken?.let { token ->
                    var bankUser = BankUserModel(bankToken = token)
                    Log.d("MainViewModel", "Create UserBank ")
                    getTransactionInfoFrom()
                    getBalanceInfoFrom()
                    uiState = uiState.copy(needAnAccess = false)
                }
            } catch (e: Exception) {
                e.localizedMessage?.let {
                    Log.d("MainViewModel", it)
                } ?: run {
                    Log.d("MainViewModel", "Undefined Error")
                }
            }
        }
    }

    /**
     * User Intent:
     * Function called on a button
     */
    fun connectToBank() {
        linkAccountToPlaid?.let { linkPlaid ->
            viewModelScope.launch {
                var cred = bankRepo.getBankLinkToken()

                val linkTokenConfiguration = linkTokenConfiguration {
                    token = cred.linkToken
                }

                // Launch The connected activity with good linkTokenConfiguration
                linkPlaid.launch(linkTokenConfiguration)
            }
        }
    }


    /**
     * Not User Intent
     * This function is the OnSuccess of the Activity Launched by Plaid DSK
     */
    fun getAccessToken() {
        uiState = uiState.copy(loading = true);
        viewModelScope.launch {
            Log.d("MainViewModel", "HOOO")
            var bankUser: BankUserModel? = null
            try {
                bankUser = userRepo.getUser()
                Log.d("MainViewModel", "Bank user : $bankUser")

            } catch (e: Exception) {
                e.localizedMessage?.let {
                    Log.d("MainViewModel", it)
                } ?: run {
                    Log.d("MainViewModel", "Undefined Error")
                }
                uiState = uiState.copy(needAnAccess = true);
            }

            bankUser?.let {
                getTransactionInfoFrom()
                getBalanceInfoFrom()
                uiState = uiState.copy(needAnAccess = false);
            } ?: run {
                uiState = uiState.copy(needAnAccess = true);
                Log.d("MainViewModel", "Cannot get BankUser")
            }
            uiState = uiState.copy(loading = false);
            Log.d("MainViewModel", "${uiState.loading}")
        }
    }
}


/**
 * Custom Factory
 */
class ExtraParamsMainViewModelFactory(
    private val application: MoneyApp,
    private val myExtraUser: KichtaUserModel
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        application.container.userRepository?.let { userRepo ->
            application.container.bankRepository?.let { bankRepo ->
                MainViewModel(user = myExtraUser, userRepo = userRepo, bankRepo = bankRepo)
            }
        } as T
}

