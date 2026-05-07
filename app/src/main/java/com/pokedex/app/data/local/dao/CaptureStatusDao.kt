package com.pokedex.app.data.local.dao

import androidx.room.*
import com.pokedex.app.data.local.entity.CaptureStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CaptureStatusDao {
    @Query("SELECT * FROM capture_status")
    fun getAllFlow(): Flow<List<CaptureStatusEntity>>

    @Query("SELECT * FROM capture_status WHERE pokemonId = :id")
    suspend fun getById(id: Int): CaptureStatusEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(status: CaptureStatusEntity)
}
