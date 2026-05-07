package com.pokedex.app.di

import android.content.Context
import androidx.room.Room
import com.pokedex.app.data.local.AppDatabase
import com.pokedex.app.data.local.dao.CaptureStatusDao
import com.pokedex.app.data.local.dao.PokemonDao
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
        Room.databaseBuilder(ctx, AppDatabase::class.java, "pokedex.db").build()

    @Provides fun providePokemonDao(db: AppDatabase): PokemonDao = db.pokemonDao()
    @Provides fun provideCaptureStatusDao(db: AppDatabase): CaptureStatusDao = db.captureStatusDao()
}
