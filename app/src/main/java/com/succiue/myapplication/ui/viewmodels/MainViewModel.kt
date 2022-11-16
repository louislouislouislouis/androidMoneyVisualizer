package com.succiue.myapplication.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.result.LinkSuccess
import com.succiue.myapplication.MoneyApp
import com.succiue.myapplication.data.model.AccountModel
import com.succiue.myapplication.data.model.UserModel
import com.succiue.myapplication.data.repository.AccountRepository
import com.succiue.myapplication.utils.sendRequest

class ExtraParamsViewModelFactory(
    private val application: MoneyApp,
    private val myExtraUser: UserModel
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MainViewModel(user = myExtraUser, application.container.accountRepository) as T
}

class MainViewModel(
    var user: UserModel,
    private val accountRepository: AccountRepository
) : ViewModel() {

    var isLoading = mutableStateOf<Boolean>(true)
    var needAnAccess = mutableStateOf<Boolean>(true)

    private val account: AccountModel = AccountModel(user)
    val publicToken = mutableStateOf("")
    val accessToken = mutableStateOf("")
    val linkToken = mutableStateOf("")


    /**
     * The laucher has to be initialized by the activity
     */
    var linkAccountToPlaid: ActivityResultLauncher<LinkTokenConfiguration>? = null

    fun onSuccess(res: LinkSuccess, ctx: Context) {
        // Send public_token to your server, exchange for access_token
        var publicTokenValue = res.publicToken
        publicToken.value = publicTokenValue
        account?.getAccessToken(ctx, accessToken, publicTokenValue, needAnAccess)

    }

    /**
     * User Intent:
     * Function called on a button
     */
    fun connectToBank(ctx: Context) {
        linkAccountToPlaid?.let {
            account.getPublicToken(
                ctx = ctx,
                linkAccountToPlaid = it,
                linkToken = linkToken
            )
        }

    }


    /**
     * Not User Intent
     * This function is the OnSuccess of the Activity Launched by Plaid DSK
     */
    fun getAccessToken(ctx: Context) {
        isLoading.value = true;
        sendRequest(
            ctx,
            "https://bankbackuqac.herokuapp.com/bank/getAccessToken",
            com.android.volley.Request.Method.POST,
            onSuccess = { response ->
                var accessToken: String
                try {
                    try {
                        accessToken = response.get("accessToken") as String
                        Log.d("ACCOUNT", accessToken)
                        needAnAccess.value = false
                    } catch (e: Exception) {
                        var test = response.get("accessToken") as Boolean
                        Log.d("ACCOUNTTT", e.localizedMessage)
                        needAnAccess.value = true
                    }

                } catch (e: Exception) {
                    Log.d("ACCOUNT-333", e.localizedMessage)
                    needAnAccess.value = true
                }
                isLoading.value = false;


            },
            onFailure = { error ->
                error?.let {
                    Log.d("ACCOUNT", it.localizedMessage)
                }
                isLoading.value = false;
            },
            owner = user
        )
    }


}