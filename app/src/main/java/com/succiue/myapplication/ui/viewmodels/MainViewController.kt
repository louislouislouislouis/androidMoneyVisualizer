package com.succiue.myapplication.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.result.LinkSuccess
import com.succiue.myapplication.LoginActivity
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
    var dummyContext: Context? = null


    var linkAccountToPlaid: ActivityResultLauncher<LinkTokenConfiguration>? = null

    fun onSuccess(res: LinkSuccess) {
        // Send public_token to your server, exchange for access_token
        var publicTokenValue = res.publicToken
        publicToken.value = publicTokenValue

        dummyContext?.let {
            account?.getAccessToken(it, accessToken, publicTokenValue)
        }
    }

    //user intent
    fun linkAccount(ctx: Context) {

        dummyContext = ctx
        if (linkAccountToPlaid != null) {
            Log.d("TEST", "fre")
            account?.getPublicToken(
                ctx = ctx,
                linkAccountToPlaid = linkAccountToPlaid!!,
                linkToken = linkToken
            )
        }
    }

    fun getAccessToken(ctx: Context) {
        isLoading.value = true;
        sendRequest(
            ctx,
            "https://bankbackuqac.herokuapp.com/bank/getAccessToken",
            com.android.volley.Request.Method.POST,
            onSuccess = { response ->
                val accessToken: String
                try {
                    accessToken = response.get("accessToken") as String
                    Log.d("ACCOUNT", accessToken)
                    needAnAccess.value = false

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

    fun connectToGoogle(ctx: Context) {
        val intent = Intent(ctx, LoginActivity::class.java)
        ctx.startActivity(intent)
    }


}