package com.pokedex.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pokedex.app.data.local.dao.CaptureStatusDao
import com.pokedex.app.data.local.dao.EvolutionEdgeDao
import com.pokedex.app.data.local.dao.PokemonDao
import com.pokedex.app.data.local.entity.CaptureStatusEntity
import com.pokedex.app.data.local.entity.EvolutionEdgeEntity
import com.pokedex.app.data.local.entity.PokemonEntity

@Database(
    entities = [PokemonEntity::class, CaptureStatusEntity::class, EvolutionEdgeEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun captureStatusDao(): CaptureStatusDao
    abstract fun evolutionEdgeDao(): EvolutionEdgeDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE pokemon ADD COLUMN evolutionChainId INTEGER DEFAULT NULL")
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS evolution_edge (
                        fromId INTEGER NOT NULL,
                        toId INTEGER NOT NULL,
                        conditions TEXT NOT NULL,
                        PRIMARY KEY(fromId, toId)
                    )
                """.trimIndent())
            }
        }
    }
}
