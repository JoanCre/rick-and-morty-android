package com.rudo.rickAndMortyApp.domain.useCase.GetCharacterDetail

import com.rudo.rickAndMortyApp.domain.entity.CharacterDetail
import com.rudo.rickAndMortyApp.domain.repository.CharactersRepository
import javax.inject.Inject

/**
 * Implementation of GetCharacterDetailUseCase for getting character detail by ID.
 * Follows Single Responsibility Principle - handles only character detail logic.
 * Follows Dependency Inversion Principle - depends on repository abstraction.
 */
class GetCharacterDetailUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetCharacterDetailUseCase {
    
    override suspend operator fun invoke(id: Int): CharacterDetail = repository.getCharacter(id)
}
