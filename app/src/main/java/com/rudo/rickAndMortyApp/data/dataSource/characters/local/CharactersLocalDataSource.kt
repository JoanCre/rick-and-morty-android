package com.rudo.rickAndMortyApp.data.dataSource.characters.local

import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.dto.CharacterDto
import kotlinx.coroutines.flow.Flow

/**
 * Enhanced local data source interface for Characters operations.
 * Now supports full character data storage for better performance and offline support.
 * Follows Interface Segregation Principle - specific contract for local operations.
 * Follows Dependency Inversion Principle - abstraction for local data access.
 */
interface CharactersLocalDataSource {
    suspend fun toggleFavorite(character: CharacterDto)
    suspend fun getFavoriteCharacterIds(): List<Int>
    suspend fun isFavorite(characterId: Int): Boolean
    fun getFavoriteCharactersFlow(): Flow<List<CharacterDto>>
}
