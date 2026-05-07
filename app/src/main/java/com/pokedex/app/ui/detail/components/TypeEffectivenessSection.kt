package com.pokedex.app.ui.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TypeEffectivenessSection(
    weaknesses: List<String>,
    resistances: List<String>,
    immunities: List<String>
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (weaknesses.isNotEmpty()) TypeRow(label = "Faiblesses", types = weaknesses)
        if (resistances.isNotEmpty()) TypeRow(label = "Résistances", types = resistances)
        if (immunities.isNotEmpty()) TypeRow(label = "Immunités", types = immunities)
    }
}

@Composable
private fun TypeRow(label: String, types: List<String>) {
    Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Gray)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        items(types) { type -> TypeBadge(type = type) }
    }
}
