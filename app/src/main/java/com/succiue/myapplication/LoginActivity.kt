package com.succiue.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.succiue.myapplication.ui.screens.LoginScreen
import com.succiue.myapplication.ui.theme.MyApplicationTheme
import com.succiue.myapplication.ui.viewmodels.LoginViewModel
import com.succiue.myapplication.ui.viewmodels.LoginViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    @Inject
    lateinit var loginViewModelFactory: LoginViewModelFactory;
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = loginViewModelFactory.create(this)

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
        loginViewModel.handleActivityResult(requestCode, data)
    }

}