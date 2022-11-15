package com.succiue.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.succiue.myapplication.ui.screens.LoginScreen
import com.succiue.myapplication.ui.viewmodels.LoginViewModel


class LoginActivity : AppCompatActivity() {

    /**
     * View Model for A Login Activity
     */
    private var loginViewModel = LoginViewModel(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),

                ) {
                LoginScreen(loginViewModel)
            }

        }
        loginViewModel.initViewModel(this)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        loginViewModel.isThereAnyPerson()
    }


    /**
     * Function called back when user finish using Plaid SDK
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginViewModel.handleActivityResult(requestCode, resultCode, data)

    }

}