package com.succiue.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

var body1 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = body1,
    displayMedium = body1,
    displaySmall = body1,
    headlineLarge = body1,
    headlineMedium = body1,
    headlineSmall = body1,
    titleLarge = body1,
    titleMedium = body1,
    titleSmall = body1,
    bodyLarge = body1,
    bodyMedium = body1,
    bodySmall = body1,
    labelLarge = body1,
    labelMedium = body1,
    labelSmall = body1
)

/* Other default text styles to override
button = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W500,
    fontSize = 14.sp
),
caption = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp
)
*/
