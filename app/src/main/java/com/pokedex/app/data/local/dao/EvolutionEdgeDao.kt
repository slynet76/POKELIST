package com.pokedex.app.data.local.dao

import androidx.room.*
import com.pokedex.app.data.local.entity.EvolutionEdgeEntity

@Dao
interface EvolutionEdgeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(edges: List<EvolutionEdgeEntity>)

    @Query("SELECT * FROM evolution_edge WHERE fromId = :id OR toId = :id")
    suspend fun getEdgesForPokemon(id: Int): List<EvolutionEdgeEntity>

    @Query("DELETE FROM evolution_edge")
    suspend fun clear()
}
