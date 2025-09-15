package com.rudo.rickAndMortyApp.domain.useCase

import com.rudo.rickAndMortyApp.domain.repository.CharactersRepository
import javax.inject.Inject

interface FavoriteUseCase {
    suspend operator fun invoke(characterId: Int)
}

class FavoriteUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : FavoriteUseCase {

    override suspend operator fun invoke(characterId: Int) {
        repository.toggleFavorite(characterId)
    }
}
