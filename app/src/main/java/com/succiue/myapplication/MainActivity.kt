package com.succiue.myapplication


//import com.succiue.myapplication.ui.viewmodels.MyViewModelFactory
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
import com.plaid.link.OpenPlaidLink
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import com.succiue.myapplication.data.model.UserModel
import com.succiue.myapplication.ui.screens.MoneyVisualizerHome
import com.succiue.myapplication.ui.theme.MyApplicationTheme
import com.succiue.myapplication.ui.viewmodels.ExtraParamsViewModelFactory
import com.succiue.myapplication.ui.viewmodels.LoginViewModel
import com.succiue.myapplication.ui.viewmodels.MainViewModel
import com.succiue.myapplication.utils.getSerializable

/**
 * This Class has to be called with an User
 * This has to be in the parameter of the intent
 */
class MainActivity : ComponentActivity() {

    /**
     * Our current User Connected
     */
    private lateinit var user: UserModel

    /**
     * The loginViewModel of our App
     */
    private var loginViewModel = LoginViewModel(this)

    /**
     * The mainViewModel of our App
     */
    private val mainAppViewModel: MainViewModel by viewModels {
        ExtraParamsViewModelFactory(application as MoneyApp, user)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get user variable
        user = intent.getSerializable("user", UserModel::class.java)
        Log.d("MainActivity", mainAppViewModel.user.toString())
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
