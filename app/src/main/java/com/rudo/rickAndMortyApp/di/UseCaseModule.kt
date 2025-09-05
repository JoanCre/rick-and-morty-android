package com.rudo.rickAndMortyApp.di

import com.rudo.rickAndMortyApp.domain.useCase.Favorite.FavoriteUseCase
import com.rudo.rickAndMortyApp.domain.useCase.Favorite.FavoriteUseCaseImpl
import com.rudo.rickAndMortyApp.domain.useCase.GetCharacterDetail.GetCharacterDetailUseCase
import com.rudo.rickAndMortyApp.domain.useCase.GetCharacterDetail.GetCharacterDetailUseCaseImpl
import com.rudo.rickAndMortyApp.domain.useCase.GetCharactersStream.GetCharactersStreamUseCase
import com.rudo.rickAndMortyApp.domain.useCase.GetCharactersStream.GetCharactersStreamUseCaseImpl
import com.rudo.rickAndMortyApp.domain.useCase.GetFavoriteCharacters.GetFavoriteCharactersUseCase
import com.rudo.rickAndMortyApp.domain.useCase.GetFavoriteCharacters.GetFavoriteCharactersUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection module for use cases.
 * Follows Dependency Inversion Principle - binds abstractions to concrete implementations.
 * Follows Single Responsibility Principle - handles only use case dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindFavoriteUseCase(
        favoriteUseCaseImpl: FavoriteUseCaseImpl
    ): FavoriteUseCase

    @Binds
    @Singleton
    abstract fun bindGetCharactersStreamUseCase(
        getCharactersStreamUseCaseImpl: GetCharactersStreamUseCaseImpl
    ): GetCharactersStreamUseCase

    @Binds
    @Singleton
    abstract fun bindGetCharacterDetailUseCase(
        getCharacterDetailUseCaseImpl: GetCharacterDetailUseCaseImpl
    ): GetCharacterDetailUseCase

    @Binds
    @Singleton
    abstract fun bindGetFavoriteCharactersUseCase(
        getFavoriteCharactersUseCaseImpl: GetFavoriteCharactersUseCaseImpl
    ): GetFavoriteCharactersUseCase
}
