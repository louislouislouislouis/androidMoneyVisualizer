package com.succiue.myapplication.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.succiue.myapplication.R

@Composable
fun LoadingScreen() {
    Icon(
        painter = painterResource(id = R.drawable.logo_app_mobile),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 100.dp),
        tint = Color.Unspecified
    )
}