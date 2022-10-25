package com.succiue.myapplication.ui.fragment

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun valueToken(text1: String,text2: String,text3: String) {
    Text(text = "Link Token : "+text1)
    Text(text = "Public Token : "+text2)
    Text(text = "Access Token : "+text3)
}
