package com.pokedex.app.data.local.dao

import androidx.room.*
import com.pokedex.app.data.local.entity.VariantAbilityEntity

@Dao
interface VariantAbilityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rows: List<VariantAbilityEntity>)

    @Query("SELECT * FROM variant_ability WHERE variantId = :variantId ORDER BY slot ASC")
    suspend fun getForVariant(variantId: Int): List<VariantAbilityEntity>

    @Query("SELECT COUNT(*) FROM variant_ability")
    suspend fun count(): Int
}
