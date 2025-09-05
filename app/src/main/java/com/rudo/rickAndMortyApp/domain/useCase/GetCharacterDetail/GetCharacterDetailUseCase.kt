package com.rudo.rickAndMortyApp.domain.useCase.GetCharacterDetail

import com.rudo.rickAndMortyApp.domain.entity.CharacterDetail

/**
 * Use case interface for getting character detail by ID.
 * Follows Interface Segregation Principle - specific contract for character detail operations.
 * Follows Dependency Inversion Principle - abstraction for character detail use case.
 */
interface GetCharacterDetailUseCase {
    suspend operator fun invoke(id: Int): CharacterDetail
}
