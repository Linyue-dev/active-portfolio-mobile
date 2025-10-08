package com.example.active_portfolio_mobile.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)


/**
 * Custom theme for the comment screen.
 * Provide a light color scheme, typography and shape
 */
@Composable
fun CommentScreenTheme(content: @Composable () -> Unit){
    //Define a custom light color scheme
    val customColors = lightColorScheme(
        primary = DarkPurple,
        onPrimary = LightText,
        background = PastelPurple,
        surface = PastelPurple,
        onSurface  =  Color.Black
    )

    //Apply the material3 them with custom colors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}




