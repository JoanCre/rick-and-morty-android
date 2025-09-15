package com.rudo.rickAndMortyApp.data.dataSource.characters.local

import com.rudo.rickAndMortyApp.domain.entity.Character
import kotlinx.coroutines.flow.Flow

/**
 * Enhanced local data source interface for Characters operations.
 * Now supports full character data storage for better performance and offline support.
 * Follows Interface Segregation Principle - specific contract for local operations.
 * Follows Dependency Inversion Principle - abstraction for local data access.
 */
interface CharactersLocalDataSource {
    suspend fun toggleFavorite(character: Character)
    suspend fun getFavoriteCharacterIds(): List<Int>
    suspend fun isFavorite(characterId: Int): Boolean
    fun getFavoriteCharactersFlow(): Flow<List<Character>>
}
