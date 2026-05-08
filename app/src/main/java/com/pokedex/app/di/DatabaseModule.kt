package com.pokedex.app.di

import android.content.Context
import androidx.room.Room
import com.pokedex.app.data.local.AppDatabase
import com.pokedex.app.data.local.dao.CaptureStatusDao
import com.pokedex.app.data.local.dao.EvolutionEdgeDao
import com.pokedex.app.data.local.dao.PokemonDao
import com.pokedex.app.data.local.dao.PokemonVariantDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "pokedex.db")
            .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3, AppDatabase.MIGRATION_3_4, AppDatabase.MIGRATION_4_5)
            .build()

    @Provides fun providePokemonDao(db: AppDatabase): PokemonDao = db.pokemonDao()
    @Provides fun provideCaptureStatusDao(db: AppDatabase): CaptureStatusDao = db.captureStatusDao()
    @Provides fun provideEvolutionEdgeDao(db: AppDatabase): EvolutionEdgeDao = db.evolutionEdgeDao()
    @Provides fun providePokemonVariantDao(db: AppDatabase): PokemonVariantDao = db.pokemonVariantDao()
}
