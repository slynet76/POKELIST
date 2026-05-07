package com.pokedex.app.ui.loading

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pokedex.app.ui.theme.PokeRed

@Composable
fun LoadingScreen(progress: Int, total: Int) {
    val fraction = if (total > 0) progress / total.toFloat() else 0f
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("PokéDex", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = PokeRed)
        Spacer(Modifier.height(24.dp))
        Text("Téléchargement du Pokédex...", fontSize = 16.sp)
        Spacer(Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier.fillMaxWidth(),
            color = PokeRed
        )
        Spacer(Modifier.height(8.dp))
        Text("$progress / $total Pokémon", fontSize = 14.sp)
    }
}
