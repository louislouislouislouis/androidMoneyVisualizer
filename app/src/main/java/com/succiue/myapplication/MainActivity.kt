package com.succiue.myapplication


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import com.plaid.link.OpenPlaidLink
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import com.succiue.myapplication.data.model.User
import com.succiue.myapplication.ui.screens.MoneyVisualizerHome
import com.succiue.myapplication.ui.theme.MyApplicationTheme
import com.succiue.myapplication.ui.viewmodels.LoginViewModel
import com.succiue.myapplication.ui.viewmodels.MainViewController
import com.succiue.myapplication.utils.getSerializable

/**
 * This Class has to be called with an User
 * This has to be in the parameter of the intent
 */
class MainActivity : ComponentActivity() {

    /**
     * Our current User Connectd
     */
    private lateinit var user: User

    /**
     * The loginViewModel of our App
     */
    private var loginViewModel = LoginViewModel(this)

    /**
     * The mainViewModel of our App
     */
    private lateinit var mainAppViewModel: MainViewController

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get user variable
        user = intent.getSerializable("user", User::class.java)

        //Create VM with user
        mainAppViewModel = MainViewController(user)

        // Create an ActivityLauncher for connect Account and give it to controller
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
        peekAvailableContext()?.let { mainAppViewModel.getAccessToken(it) }

        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginViewModel.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        loginViewModel.isThereAnyPerson()
    }
}
