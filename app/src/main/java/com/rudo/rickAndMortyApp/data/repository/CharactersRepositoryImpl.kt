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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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


    override fun getCharactersStream(searchQuery: String?): Flow<PagingData<Character>> =
        Pager(
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
        ).flow

    override suspend fun getCharacter(id: Int): CharacterDetail = withContext(Dispatchers.IO) {
        val dto = remoteDataSource.getCharacter(id)
        val isFavorite = localDataSource.isFavorite(id)
        dto.toDetail(isFavorite)
    }

    override suspend fun toggleFavorite(characterId: Int) = withContext(Dispatchers.IO) {
        // Get complete character data before toggling
        val characterDto = remoteDataSource.getCharacter(characterId)
        val character = Character(
            id = characterDto.id,
            name = characterDto.name,
            status = characterDto.status.toCharacterStatus(),
            species = characterDto.species,
            type = characterDto.type,
            gender = characterDto.gender.toCharacterGender(),
            origin = characterDto.origin.name,
            location = characterDto.location.name,
            image = characterDto.image,
            episodes = characterDto.episode,
            url = characterDto.url,
            created = characterDto.created
        )
        localDataSource.toggleFavorite(character)
    }

    override suspend fun isFavorite(characterId: Int): Boolean {
        return localDataSource.isFavorite(characterId)
    }

    override fun getFavoriteCharactersFlow(): Flow<List<Character>> {
        return localDataSource.getFavoriteCharactersFlow()
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

fun String.toCharacterStatus(): Character.Status {
    return when (lowercase()) {
        "alive" -> Character.Status.Alive
        "dead" -> Character.Status.Dead
        else -> Character.Status.Unknown
    }
}

fun String.toCharacterGender(): Character.Gender {
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

