package com.rudo.rickAndMortyApp.data.dataSource.characters.local

import com.rudo.rickAndMortyApp.data.dataSource.characters.local.dao.FavoriteCharacterDao
import com.rudo.rickAndMortyApp.data.dataSource.characters.local.dbo.FavoriteCharacterDbo
import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.dto.CharacterDto
import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.dto.LocationRefDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Local data source implementation using Room database for favorite characters.
 */
class CharactersLocalDataSourceImpl @Inject constructor(
    private val favoriteCharacterDao: FavoriteCharacterDao
) : CharactersLocalDataSource {

    override suspend fun toggleFavorite(character: CharacterDto) {
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

    override fun getFavoriteCharactersFlow(): Flow<List<CharacterDto>> {
        return favoriteCharacterDao.getFavoriteCharactersFlow().map { dboList ->
            dboList.map { it.toDto() }
        }
    }

    private fun CharacterDto.toDbo(): FavoriteCharacterDbo {
        return FavoriteCharacterDbo(
            id = id,
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender,
            origin = origin.name,
            location = location.name,
            image = image,
            episodes = episode,
            url = url,
            created = created
        )
    }

    private fun FavoriteCharacterDbo.toDto(): CharacterDto {
        return CharacterDto(
            id = id,
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender,
            origin = LocationRefDto(name = origin, url = ""),
            location = LocationRefDto(name = location, url = ""),
            image = image,
            episode = episodes,
            url = url,
            created = created
        )
    }
}
