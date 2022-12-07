package com.succiue.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.succiue.myapplication.ui.viewmodels.*
import com.succiue.myapplication.utils.getSerializable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This Class has to be called with an User
 * This has to be in the parameter of the intent
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    /**
     * Our current User Connected
     */
    private lateinit var user: KichtaUserModel


    /**
     * The mainViewModel of our App
     */
    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory;
    private lateinit var mainAppViewModel: MainViewModel

    /**
     * The LoginViewModel of our App
     */
    @Inject
    lateinit var loginViewModelFactory: LoginViewModelFactory;
    private lateinit var loginViewModel: LoginViewModel

    /**
     * The ObjectifViewModel of our App
     */
    @Inject
    lateinit var ObjectifViewModelFactory: ObjectifViewModelFactory;
    private lateinit var objectifViewModel: ObjectifViewModel


    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as MoneyApp

        // Get user variable
        user = intent.getSerializable("user", KichtaUserModel::class.java)
        mainAppViewModel = mainViewModelFactory.create(user)
        loginViewModel = loginViewModelFactory.create(this)
        objectifViewModel = ObjectifViewModelFactory.create(user)

        Log.d("MainActivity", "Current KichtaUser is : " + mainAppViewModel.user.toString())

        // Define action for activity of Plaid API
        val linkAccountToPlaid =
            registerForActivityResult(OpenPlaidLink()) {
                when (it) {
                    is LinkSuccess -> peekAvailableContext()?.let { _ ->
                        mainAppViewModel.onSuccess(it)
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
            lifecycleScope.launch {
                mainAppViewModel.getAccessToken()
            }
            Log.d("HEUUU", "HEUUU")
        }
        Log.d("HEUUU", "HEUUU2")
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MoneyVisualizerHome(
                        mainAppViewModel,
                        loginViewModel,
                        objectifViewModel,
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
        loginViewModel.handleActivityResult(requestCode, data)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        loginViewModel.autoLogin()
    }
}
