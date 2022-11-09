package com.succiue.myapplication


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
import com.succiue.myapplication.ui.screens.AppScreen
import com.succiue.myapplication.ui.screens.MoneyVizualizerHome
import com.succiue.myapplication.ui.theme.MyApplicationTheme
import com.succiue.myapplication.ui.viewmodels.MainViewController
import com.succiue.myapplication.utils.getSerializable


class MainActivity : ComponentActivity() {
    /**
     * Current User Connected.
     * This has to be in the parameter of the intent
     */
    //private lateinit var user: User


    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get user variable
        user = intent.getSerializable("user", User::class.java)

        //Create VM with user
        var viewModel = MainViewController(user)
        Log.d("USER", user.toString())

        // Create an ActivityLauncher for connect Account and give it to controller
        val linkAccountToPlaid =
            registerForActivityResult(OpenPlaidLink()) {
                when (it) {
                    is LinkSuccess -> viewModel.onSuccess(it)
                    is LinkExit -> Log.d(
                        "ConnectPlaid",
                        "Error Connecting To Bank Api"
                    )
                }
            }

        // Activate good
        viewModel.linkAccountToPlaid = linkAccountToPlaid
        peekAvailableContext()?.let { viewModel.getAccessToken(it) }

        // TODO: Change the look
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    //MoneyVizualizerHome(viewModel)
                    AppScreen()
                }
            }
        }
    }*/

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                //val windowSize = calculateWindowSizeClass(activity = this)
                AppScreen()
            }
        }
    }


}
