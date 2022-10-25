package com.succiue.myapplication


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.plaid.link.OpenPlaidLink
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import com.succiue.myapplication.ui.fragment.RandomIntButton
import com.succiue.myapplication.ui.fragment.valueToken
import com.succiue.myapplication.ui.theme.MyApplicationTheme
import com.succiue.myapplication.ui.viewmodels.AccountListViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var viewModel = AccountListViewModel()


        val linkAccountToPlaid =
            registerForActivityResult(OpenPlaidLink()) {
                when (it) {

                    is LinkSuccess -> viewModel.onSuccess(it)
                    is LinkExit -> Log.d("ConnectPlaid", "Error Connecting To Bank Api") /* handle LinkExit */
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


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MoneyVizualizerHome(viewModel: AccountListViewModel) {
    val ctx = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        valueToken(
            text1 = viewModel.linkToken.value,
            text2 = viewModel.publicToken.value,
            text3 = viewModel.accessToken.value)
        RandomIntButton(){
           viewModel.linkAccount(ctx)
        }
    }
}
