package com.pokedex.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PokedexColorScheme = lightColorScheme(
    primary = PokeRed,
    onPrimary = PokeWhite,
    primaryContainer = PokeRedDark,
    background = PokeWhite,
    surface = PokeWhite,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun PokéDexTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = PokedexColorScheme, typography = Typography, content = content)
}
