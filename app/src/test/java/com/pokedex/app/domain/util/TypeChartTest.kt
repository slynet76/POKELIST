package com.pokedex.app.domain.util

import org.junit.Assert.assertTrue
import org.junit.Test

class TypeChartTest {

    @Test
    fun `Dracaufeu feu-vol est faible contre eau et roche`() {
        val weaknesses = TypeChart.getWeaknesses("fire", "flying")
        assertTrue("water" in weaknesses)
        assertTrue("rock" in weaknesses)
    }

    @Test
    fun `Bulbizarre plante-poison n a aucune immunite`() {
        val immunities = TypeChart.getImmunities("grass", "poison")
        assertTrue(immunities.isEmpty())
    }

    @Test
    fun `Ronflex normal est immune contre spectre`() {
        val immunities = TypeChart.getImmunities("normal", null)
        assertTrue("ghost" in immunities)
    }

    @Test
    fun `Dracaufeu feu-vol a une faiblesse x4 contre roche`() {
        val mult = TypeChart.getEffectiveness("rock", "fire", "flying")
        assert(mult == 4f)
    }
}
