package com.succiue.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.succiue.myapplication.ui.screens.LoginScreen
import com.succiue.myapplication.ui.theme.MyApplicationTheme
import com.succiue.myapplication.ui.viewmodels.ExtraParamsLoginViewModelFactory
import com.succiue.myapplication.ui.viewmodels.LoginViewModel


class LoginActivity : ComponentActivity() {

    /**
     * View Model for A Login Activity
     */
    //private var loginViewModel = LoginViewModel(this)
    /**
     * The loginViewModel of our App
     */
    private val loginViewModel: LoginViewModel by viewModels {
        ExtraParamsLoginViewModelFactory(
            loginActivity = this,
            application = application as MoneyApp
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    LoginScreen(loginViewModel)
                }
            }
        }
        loginViewModel.initViewModel(this)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        loginViewModel.autoLogin()
    }


    /**
     * Function called back when user finish using Plaid SDK
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginViewModel.handleActivityResult(requestCode, resultCode, data)
    }

}