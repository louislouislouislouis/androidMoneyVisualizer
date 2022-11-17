package com.succiue.myapplication.ui.viewmodels

import android.content.Context
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

data class MainUiState(
    val loading: Boolean = false,
    val needAnAccess: Boolean = true
)

class MainViewModel(
    var user: KichtaUserModel,
    private val userRepo: UserRepository,
    private val bankRepo: BankRepository
) : ViewModel() {

    fun getBalanceInfoFrom() {
        viewModelScope.launch {
            val accountInfo = userRepo.getBalance()
            Log.d("MainViewModel", "Voila les balance$accountInfo")
        }
    }


    fun getTransactionInfoFrom() {
        viewModelScope.launch {
            val accountTR = userRepo.getTransactions()
            Log.d("MainViewModel", "Voila les balance$accountTR")
        }
    }

    var uiState by mutableStateOf(MainUiState(loading = false, needAnAccess = true))
        private set

    var credentialsBank = BankCredentialsModel()

    /**
     * The launcher has to be initialized by the activity
     */
    var linkAccountToPlaid: ActivityResultLauncher<LinkTokenConfiguration>? = null

    /**
     * Launcher callback
     */
    fun onSuccess(res: LinkSuccess, ctx: Context) {
        // Send public_token to your server, exchange for access_token
        var publicTokenValue = res.publicToken
        credentialsBank.publicToken = publicTokenValue
        viewModelScope.launch {
            try {
                bankRepo.getBankAccessToken(credentialsBank).accessToken?.let { token ->
                    var bankUser = BankUserModel(bankToken = token)
                    Log.d("MainViewModel", "Create UserBank ")
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

