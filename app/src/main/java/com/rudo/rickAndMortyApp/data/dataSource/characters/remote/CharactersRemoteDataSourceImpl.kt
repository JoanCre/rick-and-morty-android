package com.rudo.rickAndMortyApp.data.dataSource.characters.remote

import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.api.CharactersApi
import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.dto.CharacterDto
import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.dto.CharactersResponseDto
import javax.inject.Inject

/**
 * Implementation of CharactersRemoteDataSource using Retrofit API.
 * Follows Single Responsibility Principle - handles only remote API calls.
 * Follows Dependency Inversion Principle - depends on API abstraction.
 */
class CharactersRemoteDataSourceImpl @Inject constructor(
    private val api: CharactersApi
) : CharactersRemoteDataSource {
    
    override suspend fun getCharacters(page: Int, name: String?): CharactersResponseDto {
        return api.getCharacters(page, name)
    }
    
    override suspend fun getCharacter(id: Int): CharacterDto = api.getCharacter(id)
}
