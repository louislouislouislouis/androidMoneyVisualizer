package com.succiue.myapplication.ui.screens.bodies

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGoalsBody(
    modifier: Modifier = Modifier,
    onNextButtonClicked: () -> Unit
) {
    Scaffold() {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column() {
                GreetingSection("Objectifs")

                GoalFragment(goalName = "Fast Food")

                Button(onClick = onNextButtonClicked) {
                    Text(text = "Go Back")
                }
            }
        }
    }
}