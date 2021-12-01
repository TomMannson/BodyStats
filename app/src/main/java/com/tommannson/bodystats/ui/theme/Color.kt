package com.tommannson.bodystats.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

private val Slate200 = Color(0xFFff7961)
private val Slate600 = Color(0xFFba000d)
private val Slate800 = Color(0xFFf44336)

private val Orange500 = Color(0xFFF9AA33)
private val Orange700 = Color(0xFFC78522)
private val Gray = Color(0x55555555)

val AppLightColors = lightColors(
    primary = Slate800,
    onPrimary = Color.White,
    primaryVariant = Slate600,
    secondary = Orange700,
    secondaryVariant = Orange500,
    onSecondary = Color.Black,
)

val AppDarkColors = darkColors(
    primary = Slate200,
    onPrimary = Color.Black,
    secondary = Orange500,
    onSecondary = Color.Black,
).withBrandedSurface

val Colors.withBrandedSurface get() = copy(
    surface = primary.copy(alpha = 0.08f).compositeOver(this.surface),
)

val Colors.veryLightGray get() = Color.LightGray.copy(alpha = .3f)

val Colors.lighPrimary: Color
    get() = Slate200

