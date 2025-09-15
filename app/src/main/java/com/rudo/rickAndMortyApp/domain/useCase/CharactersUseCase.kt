package com.rudo.rickAndMortyApp.domain.useCase

import androidx.paging.PagingData
import com.rudo.rickAndMortyApp.domain.entity.Character
import com.rudo.rickAndMortyApp.domain.entity.CharacterDetail
import com.rudo.rickAndMortyApp.domain.repository.CharactersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CharactersUseCase {
    fun getCharactersStream(query: String? = null): Flow<PagingData<Character>>
    suspend fun getCharacterDetail(id: Int): CharacterDetail
    fun getFavoriteCharactersFlow(): Flow<List<Character>>
}

class CharactersUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : CharactersUseCase {

    override fun getCharactersStream(query: String?): Flow<PagingData<Character>> {
        return repository.getCharactersStream(query)
    }

    override suspend fun getCharacterDetail(id: Int): CharacterDetail {
        return repository.getCharacter(id)
    }

    override fun getFavoriteCharactersFlow(): Flow<List<Character>> {
        return repository.getFavoriteCharactersFlow()
    }
}
