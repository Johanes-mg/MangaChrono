package com.mangajojoh.mg.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MangaBlueScheme = darkColorScheme(
    primary = MangaBlue,
    onPrimary = MangaWhite,
    primaryContainer = MangaBlueDark,
    onPrimaryContainer = MangaWhite,
    secondary = MangaCyan,
    onSecondary = MangaBlueDeep,
    tertiary = MangaPurple,
    onTertiary = MangaWhite,
    background = MangaBlueDeep,
    onBackground = MangaWhite,
    surface = MangaBlueMid,
    onSurface = MangaWhite,
    surfaceVariant = MangaBlueDark,
    onSurfaceVariant = MangaGrayText,
    error = MangaRed,
    onError = MangaWhite
)

@Composable
fun MangaChronoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MangaBlueScheme,
        typography = Typography,
        content = content
    )
}
