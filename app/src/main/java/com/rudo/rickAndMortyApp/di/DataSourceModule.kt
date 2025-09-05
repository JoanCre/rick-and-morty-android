package com.rudo.rickAndMortyApp.di

import com.rudo.rickAndMortyApp.data.dataSource.characters.local.CharactersLocalDataSource
import com.rudo.rickAndMortyApp.data.dataSource.characters.local.CharactersLocalDataSourceImpl
import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.CharactersRemoteDataSource
import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.CharactersRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection module for data sources.
 * Follows Dependency Inversion Principle - binds abstractions to concrete implementations.
 * Follows Single Responsibility Principle - handles only data source dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindCharactersRemoteDataSource(
        charactersRemoteDataSourceImpl: CharactersRemoteDataSourceImpl
    ): CharactersRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCharactersLocalDataSource(
        charactersLocalDataSourceImpl: CharactersLocalDataSourceImpl
    ): CharactersLocalDataSource
}
