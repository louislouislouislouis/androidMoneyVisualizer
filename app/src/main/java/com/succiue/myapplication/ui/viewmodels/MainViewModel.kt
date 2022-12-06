package com.succiue.myapplication.ui.viewmodels

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.linkTokenConfiguration
import com.plaid.link.result.LinkSuccess
import com.succiue.myapplication.data.model.BankCredentialsModel
import com.succiue.myapplication.data.model.BankUserModel
import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.data.repository.DefaultBankRepo
import com.succiue.myapplication.data.repository.DefaultUserRepository
import com.succiue.myapplication.domain.GetTransactionUseCase
import com.succiue.myapplication.domain.GetUserUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

data class TransactionUiState(
    val amount: String,
    val merchant: String,
    val category: String,
    val date: String
)

data class MainBasicUiState(
    val loading: Boolean = false,
    val needAnAccess: Boolean = true,
    val name: String = "",
    val totalAmount: String = "",
    val currency: String = "",
    val transactionList: List<TransactionUiState> = listOf()
)

@AssistedFactory
interface MainViewModelFactory {
    fun create(user: KichtaUserModel): MainViewModel
}

class MainViewModel @AssistedInject constructor(
    @Assisted var user: KichtaUserModel,
) : ViewModel() {

    private val moneyData: GetTransactionUseCase =
        GetTransactionUseCase(DefaultUserRepository(user), DefaultBankRepo(user))

    private val userData: GetUserUseCase =
        GetUserUseCase(DefaultUserRepository(user), DefaultBankRepo(user))
    
    private suspend fun getBalanceInfoFrom() {
        uiState = moneyData.getBalance(uiState)
    }

    private suspend fun getTransactionInfoFrom() {
        uiState = moneyData.getTransaction(uiState)
    }

    var uiState by mutableStateOf(
        MainBasicUiState(
            loading = false,
            needAnAccess = true,
            name = user.displayName,
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
                userData.getBankAccessToken(credentialsBank).accessToken?.let { token ->
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
                var cred = userData.getBankLinkToken()

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
                bankUser = userData.getUser()
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