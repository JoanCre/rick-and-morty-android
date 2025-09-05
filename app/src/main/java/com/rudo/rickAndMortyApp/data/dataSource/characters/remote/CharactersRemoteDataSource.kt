package com.rudo.rickAndMortyApp.data.dataSource.characters.remote

import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.dto.CharacterDto
import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.dto.CharactersResponseDto

/**
 * Remote data source interface for Characters operations.
 * Specific contract for remote operations.
 * Abstraction for remote data access.
 */
interface CharactersRemoteDataSource {
    suspend fun getCharacters(page: Int = 1, name: String? = null): CharactersResponseDto
    suspend fun getCharacter(id: Int): CharacterDto
}
