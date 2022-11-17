package com.succiue.myapplication.ui.screens.bodies

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun HomeBody(navController: NavHostController, getBalanceAction: () -> Unit) {
    Text("I am the Home Body")
    Button(onClick = { getBalanceAction() }) {
        Text(text = "GET BALANCE (IN LOGCAT)")
    }
}