package com.succiue.myapplication.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@Composable
fun Profil(modifier: Modifier = Modifier, navController: NavController) {

    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Check my profil")
                    putExtra(Intent.EXTRA_TEXT, "J'ai créé mon Profil sur cette app de fou")
                }
                context.startActivity(
                    Intent.createChooser(intent, "Share my profil")
                )
            }
        ) {
            Text("Share my Profil")
        }
    }
}