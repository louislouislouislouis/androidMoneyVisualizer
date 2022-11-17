package com.succiue.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.plaid.link.OpenPlaidLink
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import com.succiue.myapplication.data.model.KichtaUserModel
import com.succiue.myapplication.ui.screens.MoneyVisualizerHome
import com.succiue.myapplication.ui.theme.MyApplicationTheme
import com.succiue.myapplication.ui.viewmodels.ExtraParamsLoginViewModelFactory
import com.succiue.myapplication.ui.viewmodels.ExtraParamsMainViewModelFactory
import com.succiue.myapplication.ui.viewmodels.LoginViewModel
import com.succiue.myapplication.ui.viewmodels.MainViewModel
import com.succiue.myapplication.utils.getSerializable
import kotlinx.coroutines.launch

/**
 * This Class has to be called with an User
 * This has to be in the parameter of the intent
 */
class MainActivity : ComponentActivity() {


    /**
     * Our current User Connected
     */
    private lateinit var user: KichtaUserModel


    /**
     * The loginViewModel of our App
     */
    private val loginViewModel: LoginViewModel by viewModels {
        ExtraParamsLoginViewModelFactory(
            loginActivity = this,
            application = application as MoneyApp
        )
    }

    /**
     * The mainViewModel of our App
     */
    private val mainAppViewModel: MainViewModel by viewModels {
        ExtraParamsMainViewModelFactory(application as MoneyApp, user)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as MoneyApp

        // Get user variable
        user = intent.getSerializable("user", KichtaUserModel::class.java)
        app.container.setUser(user)
        Log.d("MainActivity", "Current KichtaUser is : " + mainAppViewModel.user.toString())

        // Define action for activity of Plaid API
        val linkAccountToPlaid =
            registerForActivityResult(OpenPlaidLink()) {
                when (it) {
                    is LinkSuccess -> peekAvailableContext()?.let { ctx ->
                        mainAppViewModel.onSuccess(it, ctx)
                    }
                    is LinkExit -> Log.d(
                        "ConnectPlaid",
                        "Error Connecting To Bank Api"
                    )
                }
            }

        // Activate viewModel (has to be done in onCreate)
        mainAppViewModel.linkAccountToPlaid = linkAccountToPlaid
        loginViewModel.initViewModel(this)

        // Get AccessToken
        peekAvailableContext()?.let {
            Log.d("EEE", "EEEE")
            lifecycleScope.launch {
                mainAppViewModel.getAccessToken()
            }
        }

        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MoneyVisualizerHome(
                        mainAppViewModel,
                        loginViewModel,
                        calculateWindowSizeClass(this)
                    )
                }
            }
        }

    }

    @Deprecated("Just a dummy Message to make warning shut up", level = DeprecationLevel.HIDDEN)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        /**
         * Let the ViewController manage it
         */
        loginViewModel.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        loginViewModel.autoLogin()
    }
}
