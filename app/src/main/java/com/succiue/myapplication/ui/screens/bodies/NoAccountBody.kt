package com.succiue.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.succiue.myapplication.R
import com.succiue.myapplication.ui.fragment.CustomButtom
import com.succiue.myapplication.ui.fragment.valueToken
import com.succiue.myapplication.ui.viewmodels.MainViewController

@Composable
fun NoAccountBody(viewModel: MainViewController) {
    var ctx = LocalContext.current;
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Hello" + viewModel.user.displayName)
        Text(text = "U DO NOT HAVE AN ACCOUNT TO THE BANK!")
        valueToken(
            text1 = viewModel.linkToken.value,
            text2 = viewModel.publicToken.value,
            text3 = viewModel.accessToken.value
        )
        CustomButtom(stringResource(R.string.welcoming_user)) {
            viewModel.linkAccount(ctx)
        }
        Text(
            text = "Tuto : Choose any bank --> Password=pass_good ; user = user_good; and if " +
                    "mobile verfication code = 1234 "
        )

    }
}