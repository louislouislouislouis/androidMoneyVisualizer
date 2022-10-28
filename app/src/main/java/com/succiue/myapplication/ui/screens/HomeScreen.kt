package com.succiue.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.succiue.myapplication.R
import com.succiue.myapplication.ui.fragment.CustomButtom
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
        CustomButtom(stringResource(R.string.welcoming_user)) {
            viewModel.linkAccount(ctx)
        }
        CustomButtom(stringResource(R.string.connectToGoogle)) {
            viewModel.connectToGoogle(ctx)
        }
    }
}
