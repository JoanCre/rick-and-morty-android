package com.rudo.rickAndMortyApp.domain.repository

import androidx.paging.PagingData
import com.rudo.rickAndMortyApp.domain.entity.Character
import com.rudo.rickAndMortyApp.domain.entity.CharacterDetail
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    fun getCharactersStream(searchQuery: String? = null): Flow<PagingData<Character>>
    suspend fun getCharacter(id: Int): CharacterDetail

    // Favorites operations
    suspend fun toggleFavorite(characterId: Int)
    suspend fun isFavorite(characterId: Int): Boolean
    fun getFavoriteCharactersFlow(): Flow<List<Character>>
}
