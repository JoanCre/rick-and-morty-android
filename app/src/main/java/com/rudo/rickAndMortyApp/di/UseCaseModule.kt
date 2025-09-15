package com.rudo.rickAndMortyApp.di

import com.rudo.rickAndMortyApp.domain.useCase.CharactersUseCase
import com.rudo.rickAndMortyApp.domain.useCase.CharactersUseCaseImpl
import com.rudo.rickAndMortyApp.domain.useCase.FavoriteUseCase
import com.rudo.rickAndMortyApp.domain.useCase.FavoriteUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindCharactersUseCase(
        charactersUseCaseImpl: CharactersUseCaseImpl
    ): CharactersUseCase

    @Binds
    @Singleton
    abstract fun bindFavoriteUseCase(
        favoriteUseCaseImpl: FavoriteUseCaseImpl
    ): FavoriteUseCase
}
