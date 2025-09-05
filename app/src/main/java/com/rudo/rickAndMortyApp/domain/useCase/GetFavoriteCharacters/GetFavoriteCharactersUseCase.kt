package com.rudo.rickAndMortyApp.domain.useCase.GetFavoriteCharacters

import com.rudo.rickAndMortyApp.domain.entity.Character
import kotlinx.coroutines.flow.Flow

/**
 * Use case interface for getting favorite characters.
 * Follows Interface Segregation Principle - specific contract for favorite characters operations.
 * Follows Dependency Inversion Principle - abstraction for favorite characters use case.
 */
interface GetFavoriteCharactersUseCase {
    suspend operator fun invoke(): List<Character>
    
    /**
     * Get favorite character IDs as a reactive stream.
     */
    fun getFavoriteIdsFlow(): Flow<List<Int>>
}
