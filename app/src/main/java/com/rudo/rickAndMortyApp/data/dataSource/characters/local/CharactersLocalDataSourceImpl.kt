package com.rudo.rickAndMortyApp.data.dataSource.characters.local

import com.rudo.rickAndMortyApp.data.dataSource.characters.local.dao.FavoriteCharacterDao
import com.rudo.rickAndMortyApp.data.dataSource.characters.local.dbo.FavoriteCharacterDbo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of CharactersLocalDataSource using Room database.
 * Follows Single Responsibility Principle - handles only local database operations.
 * Follows Dependency Inversion Principle - depends on DAO abstraction.
 */
class CharactersLocalDataSourceImpl @Inject constructor(
    private val favoriteCharacterDao: FavoriteCharacterDao
) : CharactersLocalDataSource {

    override suspend fun toggleFavorite(characterId: Int) {
        if (favoriteCharacterDao.isFavorite(characterId)) {
            favoriteCharacterDao.removeFromFavorites(characterId)
        } else {
            favoriteCharacterDao.addToFavorites(FavoriteCharacterDbo(characterId = characterId))
        }
    }

    override suspend fun getFavoriteCharacters(): List<Int> {
        return favoriteCharacterDao.getFavoriteCharacterIds()
    }

    override suspend fun isFavorite(characterId: Int): Boolean {
        return favoriteCharacterDao.isFavorite(characterId)
    }

    override fun getFavoriteCharacterIdsFlow(): Flow<List<Int>> {
        return favoriteCharacterDao.getFavoriteCharacterIdsFlow()
    }
}
