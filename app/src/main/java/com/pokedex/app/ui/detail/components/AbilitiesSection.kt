package com.pokedex.app.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pokedex.app.domain.model.PokemonAbility
import com.pokedex.app.ui.theme.PokeRed

@Composable
fun AbilitiesSection(abilities: List<PokemonAbility>, modifier: Modifier = Modifier) {
    if (abilities.isEmpty()) return
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            "Talents",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(Modifier.height(6.dp))
        abilities.sortedBy { it.slot }.forEach { ability ->
            AbilityCard(ability)
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Composable
private fun AbilityCard(ability: PokemonAbility) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (ability.isHidden) PokeRed.copy(alpha = 0.07f) else Color(0xFFF5F5F5))
            .clickable(enabled = ability.descriptionFr != null) { expanded = !expanded }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                ability.nameFr,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )
            if (ability.isHidden) {
                Spacer(Modifier.width(8.dp))
                Text(
                    "Talent caché",
                    fontSize = 10.sp,
                    color = PokeRed,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        if (expanded && !ability.descriptionFr.isNullOrBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(
                ability.descriptionFr,
                fontSize = 12.sp,
                color = Color.DarkGray
            )
        } else if (!ability.descriptionFr.isNullOrBlank()) {
            Text(
                "Toucher pour voir l'effet",
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}
