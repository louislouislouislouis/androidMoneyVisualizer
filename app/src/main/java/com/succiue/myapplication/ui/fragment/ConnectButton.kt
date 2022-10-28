package com.succiue.myapplication.ui.fragment

import androidx.compose.material.Button
import androidx.compose.material.Text
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