package com.pokedex.app.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.pokedex.app.data.repository.PokemonRepository
import com.pokedex.app.domain.model.EvolutionEntry
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.model.PokemonForm
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
    fun `toggleCaught appelle le repository`() = runTest {
        vm.toggleCaught()
        coVerify { repo.toggleCaught(6) }
    }

    @Test
    fun `toggleShinyCaught appelle le repository`() = runTest {
        vm.toggleShinyCaught()
        coVerify { repo.toggleShinyCaught(6) }
    }

    @Test
    fun `chargement charge aussi les entrees d evolution`() = runTest {
        val entries = listOf(
            EvolutionEntry(4, "Salamèche", "url1", null),
            EvolutionEntry(5, "Reptincel", "url2", "Niveau 16"),
            EvolutionEntry(6, "Dracaufeu", "url3", "Niveau 36")
        )
        coEvery { repo.getEvolutionEntries(6) } returns entries
        // Re-instantiate VM so the stub takes effect during init
        val savedState = SavedStateHandle(mapOf("pokemonId" to 6))
        val vm2 = PokemonDetailViewModel(repo, savedState)
        assertEquals(3, vm2.uiState.value.evolutionEntries.size)
    }

    @Test
    fun `chargement charge aussi les formes`() = runTest {
        val forms = listOf(
            PokemonForm(6, 6, "default", null, "Dracaufeu", "fire", "flying", 90f, 1.7f, "u1", "u2", true)
        )
        coEvery { repo.getForms(6) } returns forms
        val savedState = SavedStateHandle(mapOf("pokemonId" to 6))
        val vm2 = PokemonDetailViewModel(repo, savedState)
        assertEquals(1, vm2.uiState.value.forms.size)
        assertEquals("Dracaufeu", vm2.uiState.value.forms.first().nameFr)
    }
}
