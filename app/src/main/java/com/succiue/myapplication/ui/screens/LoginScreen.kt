package com.succiue.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.succiue.myapplication.ui.viewmodels.LoginViewModel


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val uiState = viewModel.uiState

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(

            enabled = uiState.loginEnable,
            //colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            onClick = { viewModel.login() })
        {
            Text(text = "LOGIN WITH GOOGLE")
        }

        Button(
            //enabled = !uiState.loginEnable,
            onClick = { viewModel.logout() })
        {
            Text(text = "LOGOUT WITH GOOGLE")
        }

        if (uiState.loading) {
            Text(text = "I LOAD")
        }


    }
}
