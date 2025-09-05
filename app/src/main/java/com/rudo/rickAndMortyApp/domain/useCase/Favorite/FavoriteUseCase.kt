package com.rudo.rickAndMortyApp.domain.useCase.Favorite

/**
 * Use case interface for toggling a character's favorite status.
 * Specific contract for favorite operations.
 * Abstraction for favorite use case.
 */
interface FavoriteUseCase {
    suspend operator fun invoke(characterId: Int): Result<Unit>
}
