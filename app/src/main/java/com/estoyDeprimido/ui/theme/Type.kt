package com.estoyDeprimido.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.estoyDeprimido.R

val roboto = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_thin, FontWeight.Thin)
)
val righteous = FontFamily(
    Font(R.font.righteous_regular)
)
// Set of Material typography styles to start with
val MyTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Normal // Texto normal
    ),
    titleLarge = TextStyle(
        fontFamily = righteous,
        fontWeight = FontWeight.Normal // Para títulos grandes
    ),
    headlineMedium = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Medium // Para subtítulos
    ),
    labelSmall = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Thin // Pequeños detalles
    )
)
















