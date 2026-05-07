package com.pokedex.app.ui.list.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pokedex.app.domain.model.SwitchGame
import com.pokedex.app.ui.theme.PokeRed

@Composable
fun GameFilterChips(
    selectedGame: SwitchGame,
    onSelect: (SwitchGame) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 8.dp)
    ) {
        SwitchGame.entries.forEach { game ->
            FilterChip(
                selected = game == selectedGame,
                onClick = { onSelect(game) },
                label = { Text(game.displayName) },
                modifier = Modifier.padding(end = 6.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PokeRed,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}
