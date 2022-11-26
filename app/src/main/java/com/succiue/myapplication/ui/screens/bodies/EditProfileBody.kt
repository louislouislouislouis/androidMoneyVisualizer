package com.succiue.myapplication.ui.screens.bodies

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.succiue.myapplication.ui.viewmodels.LoginViewModel


@Composable
fun EditProfileBody(
    modifier: Modifier,
    viewModel: LoginViewModel,
    context: Context = LocalContext.current.applicationContext,
    name: String,
    onNextButtonClicked: () -> Unit
) {
    val uiState = viewModel.uiState

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        GreetingSection("Profil")

        UserInfos()

        Button(onClick = onNextButtonClicked) {
            Text(text = "Go Back")
        }
    }
}