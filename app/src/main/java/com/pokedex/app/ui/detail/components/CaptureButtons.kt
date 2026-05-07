package com.pokedex.app.ui.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pokedex.app.ui.theme.PokeGold
import com.pokedex.app.ui.theme.PokeGray
import com.pokedex.app.ui.theme.PokeRed

@Composable
fun CaptureButtons(
    isCaught: Boolean,
    isShinyCaught: Boolean,
    onToggleCaught: () -> Unit,
    onToggleShinyCaught: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            onClick = onToggleCaught,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = if (isCaught) PokeRed else PokeGray)
        ) {
            Text(if (isCaught) "✓ Capturé" else "Non capturé", color = Color.White)
        }
        Button(
            onClick = onToggleShinyCaught,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = if (isShinyCaught) PokeGold else PokeGray)
        ) {
            Text(if (isShinyCaught) "✨ Shiny !" else "✨ Shiny", color = Color.White)
        }
    }
}
