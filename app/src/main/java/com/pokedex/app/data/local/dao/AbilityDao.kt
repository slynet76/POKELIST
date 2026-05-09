package com.pokedex.app.data.local.dao

import androidx.room.*
import com.pokedex.app.data.local.entity.AbilityEntity

@Dao
interface AbilityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(abilities: List<AbilityEntity>)

    @Query("SELECT * FROM ability WHERE name IN (:names)")
    suspend fun getByNames(names: List<String>): List<AbilityEntity>

    @Query("SELECT COUNT(*) FROM ability")
    suspend fun count(): Int
}
