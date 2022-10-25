package com.succiue.myapplication.ui.fragment

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.succiue.myapplication.R


@Composable
fun RandomIntButton(
    onClick:()-> Unit
) {
        Button(onClick = {
            onClick()
        }) {
            Text(text =  stringResource(R.string.welcoming_user))
        }
}