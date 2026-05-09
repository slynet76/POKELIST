package com.pokedex.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pokedex.app.data.local.dao.AbilityDao
import com.pokedex.app.data.local.dao.CaptureStatusDao
import com.pokedex.app.data.local.dao.EvolutionEdgeDao
import com.pokedex.app.data.local.dao.PokemonDao
import com.pokedex.app.data.local.dao.PokemonVariantDao
import com.pokedex.app.data.local.dao.VariantAbilityDao
import com.pokedex.app.data.local.entity.AbilityEntity
import com.pokedex.app.data.local.entity.CaptureStatusEntity
import com.pokedex.app.data.local.entity.EvolutionEdgeEntity
import com.pokedex.app.data.local.entity.PokemonVariantEntity
import com.pokedex.app.data.local.entity.PokemonEntity
import com.pokedex.app.data.local.entity.VariantAbilityEntity

/**
 * IMPORTANT: capture_status is the ONLY table holding user data (which Pokémon they've caught).
 * All schema changes MUST preserve it. Never use fallbackToDestructiveMigration. Always provide
 * an explicit Migration that touches only the tables that need to change.
 */
@Database(
    entities = [
        PokemonEntity::class,
        CaptureStatusEntity::class,
        EvolutionEdgeEntity::class,
        PokemonVariantEntity::class,
        AbilityEntity::class,
        VariantAbilityEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun captureStatusDao(): CaptureStatusDao
    abstract fun evolutionEdgeDao(): EvolutionEdgeDao
    abstract fun pokemonVariantDao(): PokemonVariantDao
    abstract fun abilityDao(): AbilityDao
    abstract fun variantAbilityDao(): VariantAbilityDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Adds evolutionChainId to pokemon and creates evolution_edge.
                // Does NOT touch capture_status — captures are preserved.
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

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Adds the pokemon_variant table for regional forms (Galar, Alola, Hisui, Paldea).
                // Does NOT touch capture_status — captures are preserved.
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS pokemon_variant (
                        variantId INTEGER NOT NULL PRIMARY KEY,
                        speciesId INTEGER NOT NULL,
                        formName TEXT NOT NULL,
                        formLabelFr TEXT,
                        nameFr TEXT NOT NULL,
                        typePrimary TEXT NOT NULL,
                        typeSecondary TEXT,
                        weightKg REAL NOT NULL,
                        heightM REAL NOT NULL,
                        spriteUrl TEXT NOT NULL,
                        spriteShinyUrl TEXT NOT NULL,
                        isDefault INTEGER NOT NULL
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS index_pokemon_variant_speciesId ON pokemon_variant(speciesId)")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Adds stats/cries/animated columns to pokemon_variant.
                // Does NOT touch capture_status — captures are preserved.
                db.execSQL("ALTER TABLE pokemon_variant ADD COLUMN hp INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE pokemon_variant ADD COLUMN attack INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE pokemon_variant ADD COLUMN defense INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE pokemon_variant ADD COLUMN specialAttack INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE pokemon_variant ADD COLUMN specialDefense INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE pokemon_variant ADD COLUMN speed INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE pokemon_variant ADD COLUMN cryUrl TEXT")
                db.execSQL("ALTER TABLE pokemon_variant ADD COLUMN animatedSpriteUrl TEXT")
                db.execSQL("ALTER TABLE pokemon_variant ADD COLUMN animatedShinySpriteUrl TEXT")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Adds official-artwork columns. Does NOT touch capture_status — captures are preserved.
                db.execSQL("ALTER TABLE pokemon ADD COLUMN officialArtworkUrl TEXT")
                db.execSQL("ALTER TABLE pokemon_variant ADD COLUMN officialArtworkUrl TEXT")
                db.execSQL("ALTER TABLE pokemon_variant ADD COLUMN officialArtworkShinyUrl TEXT")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Adds talents/abilities tables. Does NOT touch capture_status — captures are preserved.
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS ability (
                        name TEXT NOT NULL PRIMARY KEY,
                        nameFr TEXT NOT NULL,
                        descriptionFr TEXT
                    )
                """.trimIndent())
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS variant_ability (
                        variantId INTEGER NOT NULL,
                        abilityName TEXT NOT NULL,
                        isHidden INTEGER NOT NULL,
                        slot INTEGER NOT NULL,
                        PRIMARY KEY(variantId, abilityName)
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS index_variant_ability_variantId ON variant_ability(variantId)")
            }
        }
    }
}
