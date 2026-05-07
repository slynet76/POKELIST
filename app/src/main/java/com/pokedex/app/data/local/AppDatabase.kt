package com.pokedex.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pokedex.app.data.local.dao.CaptureStatusDao
import com.pokedex.app.data.local.dao.PokemonDao
import com.pokedex.app.data.local.entity.CaptureStatusEntity
import com.pokedex.app.data.local.entity.PokemonEntity

@Database(
    entities = [PokemonEntity::class, CaptureStatusEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun captureStatusDao(): CaptureStatusDao
}
