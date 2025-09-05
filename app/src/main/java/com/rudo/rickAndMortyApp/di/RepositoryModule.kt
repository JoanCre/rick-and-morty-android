package com.rudo.rickAndMortyApp.di

import com.rudo.rickAndMortyApp.data.repository.CharactersRepositoryImpl
import com.rudo.rickAndMortyApp.domain.repository.CharactersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that binds the repository interface to its implementation.
 *
 * - Applies DIP: upper layers depend on the abstraction [CharactersRepository] because
 *   it decouples the app from concrete data implementations.
 * - Hilt injects [CharactersRepositoryImpl] whenever [CharactersRepository] is requested
 *   because this binding tells Hilt which concrete class to provide.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCharactersRepository(
        impl: CharactersRepositoryImpl
    ): CharactersRepository
}
