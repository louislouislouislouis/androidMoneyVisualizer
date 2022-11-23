package com.succiue.myapplication.ui.screens.bodies

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun StatsBody(navController: NavHostController) {
    Log.d("StatsBody", "Running Stat")
    Text("I am the Stats Page")
}