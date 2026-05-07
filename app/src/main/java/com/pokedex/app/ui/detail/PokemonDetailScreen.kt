package com.pokedex.app.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pokedex.app.domain.model.SwitchGame
import com.pokedex.app.ui.detail.components.CaptureButtons
import com.pokedex.app.ui.detail.components.TypeBadge
import com.pokedex.app.ui.detail.components.TypeEffectivenessSection
import com.pokedex.app.ui.theme.PokeRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    onBack: () -> Unit,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val pokemon = state.pokemon

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pokemon?.let { "#${it.id.toString().padStart(3,'0')} ${it.nameFr}" } ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PokeRed,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (pokemon == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PokeRed)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = pokemon.spriteUrl,
                contentDescription = pokemon.nameFr,
                modifier = Modifier.size(160.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TypeBadge(pokemon.typePrimary)
                pokemon.typeSecondary?.let { TypeBadge(it) }
            }

            HorizontalDivider()

            TypeEffectivenessSection(
                weaknesses = state.weaknesses,
                resistances = state.resistances,
                immunities = state.immunities
            )

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Taille", color = Color.Gray, fontSize = 13.sp)
                    Text("${pokemon.heightM} m", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Poids", color = Color.Gray, fontSize = 13.sp)
                    Text("${pokemon.weightKg} kg", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            HorizontalDivider()

            if (pokemon.availableInGames.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Disponible dans", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Gray)
                    Spacer(Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        pokemon.availableInGames.forEach { code ->
                            val name = SwitchGame.entries.find { it.code == code }?.displayName ?: code
                            SuggestionChip(onClick = {}, label = { Text(name, fontSize = 12.sp) })
                        }
                    }
                }
                HorizontalDivider()
            }

            CaptureButtons(
                isCaught = pokemon.isCaught,
                isShinyCaught = pokemon.isShinyCaught,
                onToggleCaught = viewModel::toggleCaught,
                onToggleShinyCaught = viewModel::toggleShinyCaught
            )
        }
    }
}
