package com.jsuka.breweryscheduler.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val Amber = Color(0xFF8C5A2B)
private val AmberLight = Color(0xFFD9A566)
private val Copper = Color(0xFF6E4A22)
private val Slate = Color(0xFF2E3440)

private val LightColors = lightColorScheme(
    primary = Amber,
    onPrimary = Color.White,
    secondary = Copper,
    onSecondary = Color.White,
    tertiary = Slate
)

private val DarkColors = darkColorScheme(
    primary = AmberLight,
    onPrimary = Color(0xFF3A2408),
    secondary = AmberLight,
    tertiary = Color(0xFFB9C2D0)
)

@Composable
fun BreweryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
