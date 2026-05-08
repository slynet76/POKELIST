package com.pokedex.app.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import com.pokedex.app.domain.model.SwitchGame
import com.pokedex.app.ui.detail.components.CaptureButtons
import com.pokedex.app.ui.detail.components.EvolutionChainSection
import com.pokedex.app.ui.detail.components.FormPage
import com.pokedex.app.ui.detail.components.FormPagerIndicator
import com.pokedex.app.ui.theme.PokeRed

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PokemonDetailScreen(
    onBack: () -> Unit,
    onEvolutionClick: (Int) -> Unit,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val pokemon = state.pokemon
    val forms = state.forms

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

        val pagerState = rememberPagerState(pageCount = { forms.size.coerceAtLeast(1) })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (forms.size > 1) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "← Glissez pour voir les autres formes →",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    FormPagerIndicator(pageCount = forms.size, current = pagerState.currentPage)
                }
            }

            if (forms.isNotEmpty()) {
                HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { page ->
                    FormPage(form = forms[page])
                }
            }

            if (pokemon.availableInGames.isNotEmpty()) {
                HorizontalDivider()
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Disponible dans", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Gray)
                    Spacer(Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        pokemon.availableInGames.forEach { code ->
                            val name = SwitchGame.entries.find { it.code == code }?.displayName ?: code
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = name,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            )
                        }
                    }
                }
            }

            if (state.evolutionEntries.isNotEmpty()) {
                HorizontalDivider()
                EvolutionChainSection(
                    entries = state.evolutionEntries,
                    currentPokemonId = pokemon.id,
                    onEvolutionClick = onEvolutionClick
                )
            }

            HorizontalDivider()

            CaptureButtons(
                isCaught = pokemon.isCaught,
                isShinyCaught = pokemon.isShinyCaught,
                onToggleCaught = viewModel::toggleCaught,
                onToggleShinyCaught = viewModel::toggleShinyCaught
            )
        }
    }
}
