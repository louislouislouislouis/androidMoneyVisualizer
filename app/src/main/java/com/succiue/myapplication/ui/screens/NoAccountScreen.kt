package com.succiue.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.succiue.myapplication.R
import com.succiue.myapplication.ui.viewmodels.MainViewModel

@Composable
fun NoAccountScreen(viewModel: MainViewModel) {
    val ctx = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Hello" + viewModel.user.displayName)
        Text(text = "U DO NOT HAVE AN ACCOUNT TO THE BANK!")
        Text(text = "LinkToken : " + viewModel.linkToken.value)
        Text(text = "PublicToken : " + viewModel.publicToken.value)
        Text(text = "AccessToken : " + viewModel.accessToken.value)
        Button(onClick = { viewModel.connectToBank(ctx) }) {
            Text(text = stringResource(R.string.welcoming_user))
        }
        Text(
            text = "Tutorial : Choose any bank --> Password=pass_good ; user = user_good; and if " +
                    "mobile verification code = 1234 "
        )

    }
}