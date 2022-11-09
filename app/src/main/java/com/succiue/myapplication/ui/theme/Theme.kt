package com.succiue.myapplication.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = DarkGrey, //Primary Color
    primaryVariant = LightGrey,
    onPrimary = LightGrey, //When on Primary Color

    secondary = Grey,
    //onSecondary = Purple500,

    background = DarkGrey,
    onBackground = Grey,

    //surface = Color.White,
    //onSurface = Purple500,
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = LightGrey, //Primary Color
    primaryVariant = Blue,
    onPrimary = DarkGrey, //When on Primary Color

    secondary = LightBlue,
    //onSecondary = Purple500,

    background = LightGrey,
    onBackground = Color.White,

//surface = Color.White,
    //onSurface = Purple500,
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette

    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}