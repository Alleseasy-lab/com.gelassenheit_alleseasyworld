package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ImmersiveDarkColorScheme = darkColorScheme(
    primary = SoftIceBlue,
    onPrimary = DeepIndigoText,
    primaryContainer = ElectricIndigo,
    onPrimaryContainer = Color.White,
    secondary = LightCerulean,
    onSecondary = DeepIndigoText,
    secondaryContainer = ImmersiveSurfaceAlt,
    onSecondaryContainer = SoftIceBlue,
    tertiary = ElectricIndigo,
    onTertiary = Color.White,
    background = ImmersiveBg,
    onBackground = TextPrimary,
    surface = ImmersiveSurface,
    onSurface = TextPrimary,
    surfaceVariant = ImmersiveSurfaceAlt,
    onSurfaceVariant = TextSecondary,
    outline = ImmersiveSurfaceBorder,
    outlineVariant = ImmersiveBorderHighlight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep immersive dark theme aesthetic consistent
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = ImmersiveDarkColorScheme,
        typography = Typography,
        content = content
    )
}
