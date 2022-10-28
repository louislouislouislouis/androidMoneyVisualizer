package com.succiue.myapplication


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.plaid.link.OpenPlaidLink
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import com.succiue.myapplication.data.model.User
import com.succiue.myapplication.ui.screens.MoneyVizualizerHome
import com.succiue.myapplication.ui.theme.MyApplicationTheme
import com.succiue.myapplication.ui.viewmodels.MainViewController
import com.succiue.myapplication.utils.getSerializable


class MainActivity : ComponentActivity() {
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = intent.getSerializable("user", User::class.java)
        Log.d("MAINACTIVITY", user.toString())
        var viewModel = MainViewController()
        val linkAccountToPlaid =
            registerForActivityResult(OpenPlaidLink()) {
                when (it) {
                    is LinkSuccess -> viewModel.onSuccess(it)
                    is LinkExit -> Log.d(
                        "ConnectPlaid",
                        "Error Connecting To Bank Api"
                    ) /* handle LinkExit */
                }
            }
        viewModel.linkAccountToPlaid = linkAccountToPlaid


        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    MoneyVizualizerHome(viewModel)
                }
            }
        }
    }
}
