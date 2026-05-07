package com.pokedex.app.ui.list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = pokemon.spriteUrl,
                    contentDescription = pokemon.nameFr,
                    modifier = Modifier.size(64.dp)
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
                    maxLines = 1
                )
            }
            Row(
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                if (pokemon.isCaught) Text("✓", fontSize = 12.sp, color = Color(0xFF4CAF50))
                if (pokemon.isShinyCaught) Text("✨", fontSize = 12.sp, color = PokeGold)
            }
        }
    }
}
