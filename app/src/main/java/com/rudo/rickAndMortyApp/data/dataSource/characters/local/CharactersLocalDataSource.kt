package com.rudo.rickAndMortyApp.data.dataSource.characters.local

import kotlinx.coroutines.flow.Flow

/**
 * Local data source interface for Characters operations.
 * Follows Interface Segregation Principle - specific contract for local operations.
 * Follows Dependency Inversion Principle - abstraction for local data access.
 */
interface CharactersLocalDataSource {
    suspend fun toggleFavorite(characterId: Int)
    suspend fun getFavoriteCharacters(): List<Int>
    suspend fun isFavorite(characterId: Int): Boolean
    fun getFavoriteCharacterIdsFlow(): Flow<List<Int>>
}
