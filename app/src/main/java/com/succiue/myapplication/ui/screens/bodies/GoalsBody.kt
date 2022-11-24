package com.succiue.myapplication.ui.screens.bodies

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
            backgroundColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
            contentColor = androidx.compose.material.MaterialTheme.colors.onBackground)
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
                
                GoalFragment(goalName = "Fast Food")

                GoalFragment(goalName = "Bar / Pub")
            }
        }
    }
}


@Composable
fun GoalFragment(
    goalName: String
){
    ElevatedCard(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = goalName,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Votre Solde",
                style = MaterialTheme.typography.headlineLarge
            )

        }
    }
}