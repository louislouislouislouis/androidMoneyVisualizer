package com.succiue.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.succiue.myapplication.ui.viewmodels.LoginViewModel


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            enabled = viewModel.loginEnable.value,
            onClick = { viewModel.login() })
        {
            Text(text = "LOGIN WITH GOOGLE")
        }
        Button(
            enabled = !viewModel.loginEnable.value,
            onClick = { viewModel.logout() })
        {
            Text(text = "LOGOUT WITH GOOGLE")
        }

    }
}
