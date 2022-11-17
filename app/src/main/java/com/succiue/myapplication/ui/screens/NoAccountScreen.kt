package com.succiue.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.succiue.myapplication.R
import com.succiue.myapplication.ui.viewmodels.MainViewModel

@Composable
fun NoAccountScreen(viewModel: MainViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Hello" + viewModel.user.displayName)
        Text(text = "U do not have an account to the Bank")

        Button(onClick = { viewModel.connectToBank() }) {
            Text(text = stringResource(R.string.welcoming_user))
        }
        Text(
            text = "Tutorial : Choose any bank --> Password=pass_good ; user = user_good; and if " +
                    "mobile verification code = 1234 "
        )

    }
}