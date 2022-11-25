package com.succiue.myapplication.ui.screens.bodies

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsBody(navController: NavHostController) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                backgroundColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                contentColor = androidx.compose.material.MaterialTheme.colors.onBackground
            )
            {
                Icon(Icons.Filled.Add, contentDescription = "Play")
            }
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End,
    ) {
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
) {
    ElevatedCard(
        modifier = Modifier
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