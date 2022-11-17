package com.succiue.myapplication.ui.screens.bodies

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun HomeBody(
    navController: NavHostController,
    getBalanceAction: () -> Unit,
    getTransactionsAction: () -> Unit

) {
    Column() {
        Text("I am the Home Body")
        Button(onClick = { getBalanceAction() }) {
            Text(text = "GET BALANCE (IN LOGCAT)")
        }
        Button(onClick = { getTransactionsAction() }) {
            Text(text = "GET TRANSACTIONS (IN LOGCAT)")
        }
    }

}