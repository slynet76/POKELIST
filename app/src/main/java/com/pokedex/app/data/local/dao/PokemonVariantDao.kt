package com.pokedex.app.data.local.dao

import androidx.room.*
import com.pokedex.app.data.local.entity.PokemonVariantEntity

@Dao
interface PokemonVariantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(variants: List<PokemonVariantEntity>)

    @Query("SELECT * FROM pokemon_variant WHERE speciesId = :speciesId ORDER BY isDefault DESC, variantId ASC")
    suspend fun getVariantsForSpecies(speciesId: Int): List<PokemonVariantEntity>

    @Query("SELECT COUNT(*) FROM pokemon_variant")
    suspend fun count(): Int

    @Query("SELECT COUNT(*) FROM pokemon_variant WHERE cryUrl IS NULL")
    suspend fun countMissingV14Data(): Int
}
