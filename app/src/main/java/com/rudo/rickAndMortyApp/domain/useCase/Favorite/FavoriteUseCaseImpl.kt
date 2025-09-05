package com.rudo.rickAndMortyApp.domain.useCase.Favorite

import com.rudo.rickAndMortyApp.domain.repository.CharactersRepository
import javax.inject.Inject

/**
 * Implementation of FavoriteUseCase for toggling a character's favorite status.
 * Handles only favorite toggling logic.
 * Depends on repository abstraction.
 */
class FavoriteUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : FavoriteUseCase {

    override suspend operator fun invoke(characterId: Int): Result<Unit> {
        return try {
            repository.toggleFavorite(characterId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
