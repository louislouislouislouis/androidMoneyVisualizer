package com.succiue.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.succiue.myapplication.ui.fragment.RandomIntButton
import com.succiue.myapplication.ui.fragment.valueToken
import com.succiue.myapplication.ui.viewmodels.AccountListViewModel


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
            text3 = viewModel.accessToken.value
        )
        RandomIntButton() {
            viewModel.linkAccount(ctx)
        }
    }
}
