package com.pokedex.app.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pokedex.app.domain.model.PokemonForm

@Composable
fun StatsSection(form: PokemonForm, modifier: Modifier = Modifier) {
    val total = form.hp + form.attack + form.defense + form.specialAttack + form.specialDefense + form.speed
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            "Statistiques de combat",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(Modifier.height(8.dp))
        StatRow("PV", form.hp, Color(0xFFE57373))
        StatRow("Attaque", form.attack, Color(0xFFF1B956))
        StatRow("Défense", form.defense, Color(0xFFEED356))
        StatRow("Atk. Spé.", form.specialAttack, Color(0xFF7DAFEA))
        StatRow("Déf. Spé.", form.specialDefense, Color(0xFF96D879))
        StatRow("Vitesse", form.speed, Color(0xFFF583A6))
        Spacer(Modifier.height(4.dp))
        Text(
            "Total : $total",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            modifier = Modifier.fillMaxWidth(),
            color = Color.DarkGray
        )
    }
}

@Composable
private fun StatRow(label: String, value: Int, color: Color) {
    // Stats range roughly 0..255. Use 200 as a "good" threshold — most bars look full around 150-200.
    val fraction = (value / 200f).coerceIn(0f, 1f)
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(
            label,
            fontSize = 11.sp,
            modifier = Modifier.width(72.dp),
            color = Color.DarkGray
        )
        Text(
            value.toString(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(36.dp),
            color = Color.DarkGray
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFEEEEEE))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction)
                    .background(color)
            )
        }
    }
}
