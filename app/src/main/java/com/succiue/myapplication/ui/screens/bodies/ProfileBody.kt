package com.succiue.myapplication.ui.screens.bodies

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.succiue.myapplication.ui.viewmodels.LoginViewModel

@Composable
fun ProfileBody(navController: NavHostController, viewModel: LoginViewModel) {
    val uiState = viewModel.uiState
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        GreetingSection("Profil")

        Button(
            enabled = !uiState.loginEnable,
            onClick = { viewModel.logout() })
        {
            Text(text = "Logout")
        }

    }

}