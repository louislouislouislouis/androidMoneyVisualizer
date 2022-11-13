package com.succiue.myapplication.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.succiue.myapplication.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StartCreateGoalScreen(modifier: Modifier = Modifier, onNextButtonClicked : () -> Unit) {
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = onNextButtonClicked, backgroundColor = MaterialTheme.colors.onBackground, contentColor = MaterialTheme.colors.onPrimary) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.create_goal))
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {

    }
}