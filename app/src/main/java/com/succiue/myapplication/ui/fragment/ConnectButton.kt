package com.succiue.myapplication.ui.fragment

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun CustomButtom(
    Text: String = "FCK",
    onClick: () -> Unit,
) {
    Button(onClick = {
        onClick()
    }) {
        Text(text = Text)

    }
}