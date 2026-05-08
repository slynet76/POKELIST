package com.pokedex.app.ui.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.ui.theme.PokeGold
import com.pokedex.app.ui.theme.typeColor

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    onClick: () -> Unit,
    onToggleCaught: () -> Unit,
    onToggleShinyCaught: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (pokemon.isCaught && pokemon.isShinyCaught)
        typeColor(pokemon.typePrimary).copy(alpha = 0.15f)
    else Color.White

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(top = 6.dp, bottom = 4.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = pokemon.officialArtworkUrl ?: pokemon.spriteUrl,
                contentDescription = pokemon.nameFr,
                modifier = Modifier.size(76.dp)
            )
            Text(
                text = "#${pokemon.id.toString().padStart(3, '0')}",
                fontSize = 10.sp,
                color = Color.Gray
            )
            Text(
                text = pokemon.nameFr,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickToggle(
                    active = pokemon.isCaught,
                    activeColor = Color(0xFF4CAF50),
                    label = "✓",
                    onClick = onToggleCaught
                )
                QuickToggle(
                    active = pokemon.isShinyCaught,
                    activeColor = PokeGold,
                    label = "✨",
                    onClick = onToggleShinyCaught
                )
            }
        }
    }
}

@Composable
private fun QuickToggle(
    active: Boolean,
    activeColor: Color,
    label: String,
    onClick: () -> Unit
) {
    val bg = if (active) activeColor else Color(0xFFE0E0E0)
    val textColor = if (active) Color.White else Color(0xFF9E9E9E)
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(bg)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontSize = 13.sp, color = textColor, fontWeight = FontWeight.Bold)
    }
}
