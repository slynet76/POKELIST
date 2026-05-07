package com.pokedex.app.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.pokedex.app.data.repository.PokemonRepository
import com.pokedex.app.domain.model.Pokemon
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDetailViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var repo: PokemonRepository
    private lateinit var vm: PokemonDetailViewModel

    private val fakePokemon = Pokemon(
        id = 6, nameFr = "Dracaufeu", typePrimary = "fire", typeSecondary = "flying",
        weightKg = 90f, heightM = 1.7f, spriteUrl = "", spriteShinyUrl = "",
        isCaught = false, isShinyCaught = false,
        availableInGames = listOf("SS", "SV")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repo = mockk(relaxed = true)
        coEvery { repo.getPokemonById(6) } returns fakePokemon
        val savedState = SavedStateHandle(mapOf("pokemonId" to 6))
        vm = PokemonDetailViewModel(repo, savedState)
    }

    @After
    fun teardown() { Dispatchers.resetMain() }

    @Test
    fun `chargement remplit l etat avec le Pokemon`() = runTest {
        assertNotNull(vm.uiState.value.pokemon)
        assertEquals("Dracaufeu", vm.uiState.value.pokemon?.nameFr)
    }

    @Test
    fun `faiblesses calculees pour feu-vol incluent eau et roche`() = runTest {
        val weaknesses = vm.uiState.value.weaknesses
        assertTrue("water" in weaknesses)
        assertTrue("rock" in weaknesses)
    }

    @Test
    fun `toggleCaught appelle le repository`() = runTest {
        vm.toggleCaught()
        coVerify { repo.toggleCaught(6) }
    }

    @Test
    fun `toggleShinyCaught appelle le repository`() = runTest {
        vm.toggleShinyCaught()
        coVerify { repo.toggleShinyCaught(6) }
    }
}
