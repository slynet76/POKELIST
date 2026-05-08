package com.pokedex.app.ui.detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pokedex.app.domain.model.PokemonForm
import com.pokedex.app.domain.util.TypeChart
import com.pokedex.app.ui.theme.PokeRed

@Composable
fun FormPage(form: PokemonForm, modifier: Modifier = Modifier) {
    val weaknesses = TypeChart.getWeaknesses(form.typePrimary, form.typeSecondary)
    val resistances = TypeChart.getResistances(form.typePrimary, form.typeSecondary)
    val immunities = TypeChart.getImmunities(form.typePrimary, form.typeSecondary)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Étiquette de forme (si pas la forme par défaut)
        form.formLabelFr?.let {
            Text(
                it,
                color = PokeRed,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // Sprites côte-à-côte
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FloatingArtwork(
                    model = form.officialArtworkUrl ?: form.animatedSpriteUrl ?: form.spriteUrl,
                    contentDescription = "${form.nameFr} normal",
                    modifier = Modifier.size(140.dp)
                )
                Text("Normal", fontSize = 12.sp, color = Color.Gray)
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FloatingArtwork(
                    model = form.officialArtworkShinyUrl ?: form.animatedShinySpriteUrl ?: form.spriteShinyUrl,
                    contentDescription = "${form.nameFr} shiny",
                    modifier = Modifier.size(140.dp),
                    phaseOffsetMs = 350
                )
                Text("✨ Shiny", fontSize = 12.sp, color = Color.Gray)
            }
        }

        // Types
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TypeBadge(form.typePrimary)
            form.typeSecondary?.let { TypeBadge(it) }
        }

        HorizontalDivider()

        TypeEffectivenessSection(
            weaknesses = weaknesses,
            resistances = resistances,
            immunities = immunities
        )

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Taille", color = Color.Gray, fontSize = 13.sp)
                Text("${form.heightM} m", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Poids", color = Color.Gray, fontSize = 13.sp)
                Text("${form.weightKg} kg", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        HorizontalDivider()

        CryButton(cryUrl = form.cryUrl, modifier = Modifier.fillMaxWidth())

        HorizontalDivider()

        StatsSection(form = form)
    }
}
