package com.pokedex.app.ui.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pokedex.app.domain.model.EvolutionEntry

@Composable
fun EvolutionChainSection(
    entries: List<EvolutionEntry>,
    currentPokemonId: Int,
    onEvolutionClick: (Int) -> Unit
) {
    if (entries.isEmpty()) return
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Évolutions",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(entries, key = { it.pokemonId }) { entry ->
                EvolutionItem(
                    entry = entry,
                    isCurrent = entry.pokemonId == currentPokemonId,
                    onClick = { onEvolutionClick(entry.pokemonId) }
                )
            }
        }
    }
}

@Composable
private fun EvolutionItem(
    entry: EvolutionEntry,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(96.dp)
            .clickable(enabled = !isCurrent, onClick = onClick)
            .padding(4.dp)
    ) {
        entry.conditionFromPredecessor?.let { cond ->
            Text(
                "↓ $cond",
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            Spacer(Modifier.height(4.dp))
        }
        AsyncImage(
            model = entry.spriteUrl,
            contentDescription = entry.nameFr,
            modifier = Modifier.size(72.dp)
        )
        Text(
            "#${entry.pokemonId.toString().padStart(3, '0')}",
            fontSize = 9.sp,
            color = Color.Gray
        )
        Text(
            entry.nameFr,
            fontSize = 11.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
