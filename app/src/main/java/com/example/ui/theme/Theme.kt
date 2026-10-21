package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val StudyPilotLightColorScheme = lightColorScheme(
    primary = StudyPilotPrimary,
    onPrimary = Color.White,
    primaryContainer = StudyPilotPrimaryLight,
    onPrimaryContainer = StudyPilotPrimary,
    secondary = StudyPilotCyan,
    onSecondary = Color.White,
    secondaryContainer = StudyPilotCyanBg,
    onSecondaryContainer = StudyPilotCyan,
    tertiary = StudyPilotPurple,
    background = StudyPilotBg,
    onBackground = StudyPilotTextDark,
    surface = StudyPilotSurface,
    onSurface = StudyPilotTextDark,
    surfaceVariant = StudyPilotBg,
    onSurfaceVariant = StudyPilotTextMuted,
    outline = StudyPilotBorder,
    outlineVariant = StudyPilotDivider
)

private val StudyPilotDarkColorScheme = darkColorScheme(
    primary = StudyPilotPrimary,
    onPrimary = Color.White,
    primaryContainer = FocusDarkCard,
    onPrimaryContainer = Color.White,
    secondary = StudyPilotCyan,
    background = FocusDarkBg,
    onBackground = Color.White,
    surface = FocusDarkSurface,
    onSurface = Color.White,
    outline = FocusDarkBorder
)

@Composable
fun StudyPilotTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) StudyPilotDarkColorScheme else StudyPilotLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
