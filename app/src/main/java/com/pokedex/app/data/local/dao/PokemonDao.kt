package com.pokedex.app.data.local.dao

import androidx.room.*
import com.pokedex.app.data.local.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon ORDER BY id ASC")
    fun getAllFlow(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon WHERE id IN (:ids) ORDER BY id ASC")
    fun getByIdsFlow(ids: List<Int>): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getById(id: Int): PokemonEntity?

    @Query("SELECT COUNT(*) FROM pokemon")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemon: List<PokemonEntity>)

    @Query("SELECT COUNT(*) FROM pokemon WHERE evolutionChainId IS NULL")
    suspend fun countMissingEvolutionData(): Int

    @Query("SELECT id FROM pokemon WHERE evolutionChainId = :chainId")
    suspend fun getIdsInChain(chainId: Int): List<Int>

    @Query("SELECT COUNT(*) FROM pokemon WHERE officialArtworkUrl IS NULL")
    suspend fun countMissingArtwork(): Int
}
