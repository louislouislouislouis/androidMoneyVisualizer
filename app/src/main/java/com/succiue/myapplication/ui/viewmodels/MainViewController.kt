package com.succiue.myapplication.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.result.LinkSuccess
import com.succiue.myapplication.data.model.Account
import com.succiue.myapplication.data.model.User
import com.succiue.myapplication.utils.sendRequest


class MainViewController(var user: User) : ViewModel() {

    var isLoading = mutableStateOf<Boolean>(true)
    var needAnAccess = mutableStateOf<Boolean>(true)

    private val account: Account = Account(user)
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
                Log.d("ACCOUNT", error.localizedMessage)
                isLoading.value = false;
            },
            owner = user
        )
    }


}