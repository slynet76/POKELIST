package com.pokedex.app.ui.list.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pokedex.app.ui.theme.PokeGold
import com.pokedex.app.ui.theme.PokeRed

@Composable
fun ProgressBarsSection(
    caughtCount: Int,
    shinyCaughtCount: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Surface(shadowElevation = 4.dp, modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            ProgressRow(label = "Normal", count = caughtCount, total = total, color = PokeRed)
            Spacer(Modifier.height(6.dp))
            ProgressRow(label = "Shiny ✨", count = shinyCaughtCount, total = total, color = PokeGold)
        }
    }
}

@Composable
private fun ProgressRow(label: String, count: Int, total: Int, color: Color) {
    val fraction = if (total > 0) count / total.toFloat() else 0f
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        Text("$count / $total", fontSize = 13.sp, color = Color.Gray)
    }
    Spacer(Modifier.height(2.dp))
    LinearProgressIndicator(progress = { fraction }, modifier = Modifier.fillMaxWidth(), color = color)
}
