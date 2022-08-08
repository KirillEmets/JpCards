package com.kirillemets.flashcards.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColorScheme(
    primary = Purple,
    secondary = Teal200,
    onPrimary = Color.White,
)

private val DarkColorPalette = darkColorScheme(
    primary = Purple,
    secondary = Teal200,
    onPrimary = Color.White,
)

@Composable
fun AppTheme(
    content: @Composable() () -> Unit
) {
    val colors = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}