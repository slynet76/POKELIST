package com.pokedex.app.ui.list

import com.pokedex.app.data.repository.PokemonRepository
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.model.SwitchGame
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokedexListViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var repo: PokemonRepository
    private lateinit var vm: PokedexListViewModel

    private val fakePokemon = listOf(
        Pokemon(1, "Bulbizarre", "grass", "poison", 6.9f, 0.7f, "", "", isCaught = true,  isShinyCaught = false),
        Pokemon(4, "Salameche",  "fire",  null,     8.5f, 0.6f, "", "", isCaught = false, isShinyCaught = true),
        Pokemon(7, "Carapuce",   "water", null,     9.0f, 0.5f, "", "", isCaught = false, isShinyCaught = false),
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repo = mockk(relaxed = true)
        every { repo.getAllPokemonFlow() } returns flowOf(fakePokemon)
        every { repo.getPokemonByGameFlow(any()) } returns flowOf(fakePokemon)
        vm = PokedexListViewModel(repo)
    }

    @After
    fun teardown() { Dispatchers.resetMain() }

    @Test
    fun `etat initial contient tous les Pokemon`() {
        assertEquals(3, vm.uiState.value.pokemon.size)
    }

    @Test
    fun `selectGame met a jour selectedGame`() {
        vm.selectGame(SwitchGame.SV)
        assertEquals(SwitchGame.SV, vm.uiState.value.selectedGame)
    }

    @Test
    fun `caughtCount compte les pokemon captures`() {
        assertEquals(1, vm.uiState.value.caughtCount)
        assertEquals(3, vm.uiState.value.totalCount)
    }

    @Test
    fun `shinyCaughtCount compte les shinys captures`() {
        assertEquals(1, vm.uiState.value.shinyCaughtCount)
    }

    @Test
    fun `search filtre la liste par nom`() {
        vm.search("bul")
        val names = vm.uiState.value.pokemon.map { it.nameFr }
        assertEquals(listOf("Bulbizarre"), names)
    }
}
