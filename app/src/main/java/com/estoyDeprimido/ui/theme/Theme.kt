package com.estoyDeprimido.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val LightColorScheme = lightColorScheme(
    primary = Purple,
    secondary = Pink,
    onSecondary = PinkDark,
    background = GreyBG,
    onBackground = Beige,

)

@Composable
fun EatITv_02Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = MyTypography,
        content = content
    )
}