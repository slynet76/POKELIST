package com.pokedex.app.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pokedex.app.ui.theme.typeColor

val typeNamesFr = mapOf(
    "normal" to "Normal", "fire" to "Feu", "water" to "Eau", "electric" to "Électrik",
    "grass" to "Plante", "ice" to "Glace", "fighting" to "Combat", "poison" to "Poison",
    "ground" to "Sol", "flying" to "Vol", "psychic" to "Psy", "bug" to "Insecte",
    "rock" to "Roche", "ghost" to "Spectre", "dragon" to "Dragon", "dark" to "Ténèbres",
    "steel" to "Acier", "fairy" to "Fée"
)

@Composable
fun TypeBadge(type: String, modifier: Modifier = Modifier) {
    Text(
        text = typeNamesFr[type] ?: type.replaceFirstChar { it.uppercase() },
        color = Color.White,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .background(typeColor(type), RoundedCornerShape(50))
            .padding(horizontal = 14.dp, vertical = 4.dp)
    )
}
