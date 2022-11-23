package com.succiue.myapplication.ui.screens.bodies

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.succiue.myapplication.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsBody(navController: NavHostController) {

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {},
            backgroundColor = androidx.compose.material.MaterialTheme.colors.onBackground,
            contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary)
        {
            Icon(Icons.Filled.Add, contentDescription = "Play")
        }
    },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End,) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column() {
                GreetingSection("Objectifs")
            }
        }
    }
}