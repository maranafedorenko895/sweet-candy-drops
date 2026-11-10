package com.Qesnisu9.Madqis.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CandyColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryWarm,
    onPrimaryContainer = OnPrimary,
    secondary = Secondary,
    onSecondary = OnPrimary,
    secondaryContainer = SurfaceSunken,
    onSecondaryContainer = TextPrimary,
    tertiary = Accent,
    onTertiary = TextPrimary,
    background = BackgroundTop,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceSunken,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceOutline,
    outlineVariant = SurfaceOutline,
    error = Primary,
    onError = OnPrimary
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CandyColorScheme,
        typography = CandyTypography,
        content = content
    )
}
