package com.tommannson.bodystats.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ApplicationTheme(useDarkColors: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (useDarkColors) AppDarkColors else AppLightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}

