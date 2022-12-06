package com.succiue.myapplication.ui.screens.bodies

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.succiue.myapplication.ui.viewmodels.ObjectifViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsBody(
    modifier: Modifier = Modifier,
    onNextButtonClicked: () -> Unit,
    ObjectifViewModel: ObjectifViewModel = hiltViewModel()
) {
    val objList = ObjectifViewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNextButtonClicked,
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
                Button(onClick = { ObjectifViewModel.test() }) {
                    Text("TEST")
                }
                Button(onClick = { ObjectifViewModel.test2() }) {
                    Text("TEST2")
                }
                LazyColumn() {
                    items(count = objList.value.size,
                        contentType = {
                            it
                        }) {
                        Card(modifier = modifier.padding(8.dp)) {
                            Row() {
                                Text(
                                    text = objList.value[it].amount,
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
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