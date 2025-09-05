package com.rudo.rickAndMortyApp.domain.useCase.GetCharactersStream

import androidx.paging.PagingData
import com.rudo.rickAndMortyApp.domain.entity.Character
import com.rudo.rickAndMortyApp.domain.repository.CharactersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of GetCharactersStreamUseCase for getting characters stream with pagination.
 * Follows Single Responsibility Principle - handles only characters stream logic.
 * Follows Dependency Inversion Principle - depends on repository abstraction.
 */
class GetCharactersStreamUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetCharactersStreamUseCase {
    
    override operator fun invoke(searchQuery: String?): Flow<PagingData<Character>> = repository.getCharactersStream(searchQuery)
}
