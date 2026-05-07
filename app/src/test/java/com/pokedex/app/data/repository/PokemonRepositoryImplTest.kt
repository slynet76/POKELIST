package com.pokedex.app.data.repository

import com.pokedex.app.data.local.dao.CaptureStatusDao
import com.pokedex.app.data.local.dao.PokemonDao
import com.pokedex.app.data.local.entity.CaptureStatusEntity
import com.pokedex.app.data.remote.PokeApiService
import com.pokedex.app.util.PreferencesManager
import com.pokedex.app.util.SwitchGamesLoader
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PokemonRepositoryImplTest {

    private lateinit var pokemonDao: PokemonDao
    private lateinit var captureStatusDao: CaptureStatusDao
    private lateinit var api: PokeApiService
    private lateinit var prefs: PreferencesManager
    private lateinit var gamesLoader: SwitchGamesLoader
    private lateinit var repo: PokemonRepositoryImpl

    @Before
    fun setup() {
        pokemonDao = mockk(relaxed = true)
        captureStatusDao = mockk(relaxed = true)
        api = mockk()
        prefs = mockk(relaxed = true)
        gamesLoader = mockk(relaxed = true)
        repo = PokemonRepositoryImpl(pokemonDao, captureStatusDao, api, prefs, gamesLoader)
    }

    @Test
    fun `toggleCaught insere un nouveau statut si absent`() = runTest {
        coEvery { captureStatusDao.getById(25) } returns null
        repo.toggleCaught(25)
        coVerify { captureStatusDao.upsert(CaptureStatusEntity(pokemonId = 25, isCaught = true, isShinyCaught = false)) }
    }

    @Test
    fun `toggleCaught inverse isCaught si deja present`() = runTest {
        val existing = CaptureStatusEntity(pokemonId = 25, isCaught = true, isShinyCaught = false)
        coEvery { captureStatusDao.getById(25) } returns existing
        repo.toggleCaught(25)
        coVerify { captureStatusDao.upsert(existing.copy(isCaught = false)) }
    }

    @Test
    fun `toggleShinyCaught insere un nouveau statut si absent`() = runTest {
        coEvery { captureStatusDao.getById(25) } returns null
        repo.toggleShinyCaught(25)
        coVerify { captureStatusDao.upsert(CaptureStatusEntity(pokemonId = 25, isCaught = false, isShinyCaught = true)) }
    }

    @Test
    fun `needsInitialSync retourne true si base vide`() = runTest {
        coEvery { pokemonDao.count() } returns 0
        assertEquals(true, repo.needsInitialSync())
    }

    @Test
    fun `needsInitialSync retourne false si base pleine`() = runTest {
        coEvery { pokemonDao.count() } returns 1025
        assertEquals(false, repo.needsInitialSync())
    }
}
