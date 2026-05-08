package com.pokedex.app.ui.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pokedex.app.domain.model.EvolutionEntry
import com.pokedex.app.ui.theme.PokeRed

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
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
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
    val containerColor = if (isCurrent) PokeRed.copy(alpha = 0.08f) else Color.Transparent

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(110.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(enabled = !isCurrent, onClick = onClick)
            .padding(vertical = 6.dp, horizontal = 4.dp)
    ) {
        // Zone "condition" : hauteur fixe pour aligner tous les items, même sans condition.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp),
            contentAlignment = Alignment.Center
        ) {
            entry.conditionFromPredecessor?.let { cond ->
                Text(
                    "↓ $cond",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    lineHeight = 12.sp
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        AsyncImage(
            model = entry.spriteUrl,
            contentDescription = entry.nameFr,
            modifier = Modifier.size(72.dp)
        )

        Spacer(Modifier.height(4.dp))

        Text(
            "#${entry.pokemonId.toString().padStart(3, '0')}",
            fontSize = 9.sp,
            color = Color.Gray
        )

        Text(
            entry.nameFr,
            fontSize = 11.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )

        // Zone "forme" : hauteur fixe pour conserver l'alignement entre items avec/sans label.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(22.dp),
            contentAlignment = Alignment.Center
        ) {
            entry.formLabel?.let { label ->
                Text(
                    label,
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Italic,
                    color = PokeRed,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}
