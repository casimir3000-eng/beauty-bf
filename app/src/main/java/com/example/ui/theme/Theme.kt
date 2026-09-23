package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = TerracottaPrimary,
    onPrimary = Color.White,
    primaryContainer = TerracottaSoftBg,
    onPrimaryContainer = TerracottaPrimaryDark,
    secondary = AmberGoldSecondary,
    onSecondary = Color.White,
    secondaryContainer = AmberSoftBg,
    onSecondaryContainer = AmberGoldDark,
    tertiary = TerracottaPrimaryLight,
    onTertiary = Color.White,
    background = WarmCreamSurface,
    onBackground = TextPrimaryDark,
    surface = Color.White,
    onSurface = TextPrimaryDark,
    surfaceVariant = WarmSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = WarmSurfaceBorder,
    error = ErrorRed,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = TerracottaPrimaryLight,
    onPrimary = Color(0xFF490016),
    primaryContainer = TerracottaPrimaryDark,
    onPrimaryContainer = Color(0xFFFFD9DF),
    secondary = AmberGoldLight,
    onSecondary = Color(0xFF452B00),
    secondaryContainer = AmberGoldDark,
    onSecondaryContainer = Color(0xFFFFDEA8),
    background = DarkSurface,
    onBackground = DarkTextPrimary,
    surface = DarkSurfaceVariant,
    onSurface = DarkTextPrimary,
    surfaceVariant = Color(0xFF3B2A2F),
    onSurfaceVariant = DarkTextSecondary,
    outline = Color(0xFF5A4349),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our handcrafted luxury brand palette
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
