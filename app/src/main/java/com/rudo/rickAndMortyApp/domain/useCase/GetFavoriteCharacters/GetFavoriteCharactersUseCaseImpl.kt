package com.rudo.rickAndMortyApp.domain.useCase.GetFavoriteCharacters

import com.rudo.rickAndMortyApp.domain.entity.Character
import com.rudo.rickAndMortyApp.domain.repository.CharactersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of GetFavoriteCharactersUseCase for getting favorite characters.
 * Follows Single Responsibility Principle - handles only favorite characters retrieval.
 * Follows Dependency Inversion Principle - depends on repository abstraction.
 */
class GetFavoriteCharactersUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetFavoriteCharactersUseCase {
    
    override suspend operator fun invoke(): List<Character> {
        return repository.getFavoriteCharacters()
    }
    
    override fun getFavoriteIdsFlow(): Flow<List<Int>> {
        return repository.getFavoriteCharacterIds()
    }
}
