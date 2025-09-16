package com.rudo.rickAndMortyApp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rudo.rickAndMortyApp.data.dataSource.characters.local.CharactersLocalDataSource
import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.CharactersRemoteDataSource
import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.dto.CharacterDto
import com.rudo.rickAndMortyApp.data.paging.CharactersPagingSource
import com.rudo.rickAndMortyApp.domain.entity.Character
import com.rudo.rickAndMortyApp.domain.entity.CharacterDetail
import com.rudo.rickAndMortyApp.domain.entity.EpisodeRef
import com.rudo.rickAndMortyApp.domain.repository.CharactersRepository
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository implementation that coordinates data from network (Retrofit) and local DB (Room)
 * and exposes domain-friendly APIs to upper layers.
 *
 * Integration points:
 * - Paging 3: Uses [Pager] with [CharactersPagingSource] to stream paginated [Character].
 * - Retrofit: Fetches character pages and details via [CharactersRemoteDataSource].
 * - Room: Persists favorites and exposes them via [CharactersLocalDataSource].
 *
 * Clean Architecture note: Domain types are returned to callers; DTO/DB types are kept inside data layer.
 */
class CharactersRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharactersRemoteDataSource,
    private val localDataSource: CharactersLocalDataSource
) : CharactersRepository {

    override fun getCharactersStream(searchQuery: String?): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 3
            ),
            pagingSourceFactory = {
                CharactersPagingSource(
                    remoteDataSource = remoteDataSource,
                    localDataSource = localDataSource,
                    searchQuery = searchQuery
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { dto ->
                val isFavorite = localDataSource.isFavorite(dto.id)
                dto.toCharacter(isFavorite)
            }
        }
    }

    override suspend fun getCharacter(id: Int): CharacterDetail = withContext(Dispatchers.IO) {
        val dto = remoteDataSource.getCharacter(id)
        val isFavorite = localDataSource.isFavorite(id)
        dto.toDetail(isFavorite)
    }

    override suspend fun toggleFavorite(characterId: Int) = withContext(Dispatchers.IO) {
        val characterDto = remoteDataSource.getCharacter(characterId)
        localDataSource.toggleFavorite(characterDto)
    }

    override suspend fun isFavorite(characterId: Int): Boolean {
        return localDataSource.isFavorite(characterId)
    }

    override fun getFavoriteCharactersFlow(): Flow<List<Character>> {
        return localDataSource.getFavoriteCharactersFlow().map { dtos ->
            dtos.map { it.toCharacter(isFavorite = true) }
        }
    }

    private fun CharacterDto.toDetail(isFavorite: Boolean = false): CharacterDetail {
        return CharacterDetail(
            id = id,
            name = name,
            status = status.toCharacterStatus(),
            species = species.ifBlank { "Unknown" },
            type = type.ifBlank { "-" },
            originName = origin.name,
            originUrl = origin.url,
            image = image,
            episodes = episode.toEpisodeRefs(),
            isFavorite = isFavorite
        )
    }
}

private fun CharacterDto.toCharacter(isFavorite: Boolean = false): Character {
    return Character(
        id = id,
        name = name,
        status = status.toCharacterStatus(),
        species = species,
        type = type,
        gender = gender.toCharacterGender(),
        origin = origin.name,
        location = location.name,
        image = image,
        episodes = episode,
        url = url,
        created = created,
        isFavorite = isFavorite
    )
}

private fun String.toCharacterStatus(): Character.Status {
    return when (lowercase()) {
        "alive" -> Character.Status.Alive
        "dead" -> Character.Status.Dead
        else -> Character.Status.Unknown
    }
}

private fun String.toCharacterGender(): Character.Gender {
    return when (lowercase()) {
        "male" -> Character.Gender.Male
        "female" -> Character.Gender.Female
        "genderless" -> Character.Gender.Genderless
        else -> Character.Gender.Unknown
    }
}

private fun List<String>.toEpisodeRefs(): List<EpisodeRef> {
    return mapNotNull { url ->
        val idPart = url.substringAfterLast('/', "").toIntOrNull()
        idPart?.let { EpisodeRef(id = it, url = url) }
    }
}

