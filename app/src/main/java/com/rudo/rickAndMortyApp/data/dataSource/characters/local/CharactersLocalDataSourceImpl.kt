package com.rudo.rickAndMortyApp.data.dataSource.characters.local

import com.rudo.rickAndMortyApp.data.dataSource.characters.local.dao.FavoriteCharacterDao
import com.rudo.rickAndMortyApp.data.dataSource.characters.local.dbo.toCharacter
import com.rudo.rickAndMortyApp.data.dataSource.characters.local.dbo.toDbo
import com.rudo.rickAndMortyApp.domain.entity.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Local data source implementation using Room database for favorite characters.
 */
class CharactersLocalDataSourceImpl @Inject constructor(
    private val favoriteCharacterDao: FavoriteCharacterDao
) : CharactersLocalDataSource {

    override suspend fun toggleFavorite(character: Character) {
        if (favoriteCharacterDao.isFavorite(character.id)) {
            favoriteCharacterDao.removeFromFavorites(character.id)
        } else {
            favoriteCharacterDao.addToFavorites(character.toDbo())
        }
    }

    override suspend fun getFavoriteCharacterIds(): List<Int> {
        return favoriteCharacterDao.getFavoriteCharacterIds()
    }

    override suspend fun isFavorite(characterId: Int): Boolean {
        return favoriteCharacterDao.isFavorite(characterId)
    }

    override fun getFavoriteCharactersFlow(): Flow<List<Character>> {
        return favoriteCharacterDao.getFavoriteCharactersFlow().map { dboList ->
            dboList.map { it.toCharacter() }
        }
    }
}
