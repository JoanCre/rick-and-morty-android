package com.rudo.rickAndMortyApp.domain.useCase.GetCharactersStream

import androidx.paging.PagingData
import com.rudo.rickAndMortyApp.domain.entity.Character
import kotlinx.coroutines.flow.Flow

/**
 * Use case interface for getting characters stream with pagination.
 * Follows Interface Segregation Principle - specific contract for characters stream operations.
 * Follows Dependency Inversion Principle - abstraction for characters stream use case.
 */
interface GetCharactersStreamUseCase {
    operator fun invoke(searchQuery: String? = null): Flow<PagingData<Character>>
}
