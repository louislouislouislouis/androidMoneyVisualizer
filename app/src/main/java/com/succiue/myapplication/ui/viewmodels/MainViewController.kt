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


class MainViewController() : ViewModel() {

    private val account: Account = Account()
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

    fun connectToGoogle(ctx: Context) {
        Log.d("TEST", "Try connect")
        val intent = Intent(ctx, LoginActivity::class.java)
        ctx.startActivity(intent)
        Log.d("TEST", "Try connect")
    }


}