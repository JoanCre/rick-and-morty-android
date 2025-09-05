package com.rudo.rickAndMortyApp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.CharactersRemoteDataSource
import com.rudo.rickAndMortyApp.data.dataSource.characters.local.CharactersLocalDataSource
import com.rudo.rickAndMortyApp.data.paging.CharactersPagingSource
import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.dto.CharacterDto
import com.rudo.rickAndMortyApp.domain.entity.Character
import com.rudo.rickAndMortyApp.domain.entity.CharacterDetail
import com.rudo.rickAndMortyApp.domain.entity.EpisodeRef
import com.rudo.rickAndMortyApp.domain.repository.CharactersRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

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


    /**
     * Returns a flow of paginated characters filtered by optional [searchQuery].
     *
     * - PagingSource fetches from network and merges favorite state from Room.
     * - Domain models are emitted (no DTO leaks).
     */
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

    /**
     * Loads a single character detail from the network and augments it with favorite state from Room.
     */
    override suspend fun getCharacter(id: Int): CharacterDetail {
        val dto = remoteDataSource.getCharacter(id)
        val isFavorite = localDataSource.isFavorite(id)
        return dto.toDetail(isFavorite)
    }

    /** Toggles favorite state of a character and persists in Room. */
    override suspend fun toggleFavorite(characterId: Int) {
        localDataSource.toggleFavorite(characterId)
    }

    /** Returns current favorite state from Room. */
    override suspend fun isFavorite(characterId: Int): Boolean {
        return localDataSource.isFavorite(characterId)
    }

    /** Emits the set of favorite character IDs stored in Room as a Flow. */
    override fun getFavoriteCharacterIds(): Flow<List<Int>> {
        return localDataSource.getFavoriteCharacterIdsFlow()
    }

    /**
     * Returns the list of favorite characters by resolving their details via network.
     * Used for the Favorites tab without performing full pagination.
     */
    override suspend fun getFavoriteCharacters(): List<Character> {
        val favoriteIds = localDataSource.getFavoriteCharacters()
        return favoriteIds.mapNotNull { id ->
            try {
                val dto = remoteDataSource.getCharacter(id)
                Character(
                    id = dto.id,
                    name = dto.name,
                    status = dto.status.toCharacterStatus(),
                    origin = dto.origin.name,
                    image = dto.image,
                    isFavorite = true
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Extension function to map CharacterDto to CharacterDetail domain entity.
     * Mapper functions in repository layer.
     */
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

/**
 * Extension functions for mapping DTOs to domain entities.
 * Centralized mapping logic to avoid duplication.
 */
fun String.toCharacterStatus(): Character.Status {
    return when (lowercase()) {
        "alive" -> Character.Status.Alive
        "dead" -> Character.Status.Dead
        else -> Character.Status.Unknown
    }
}

private fun List<String>.toEpisodeRefs(): List<EpisodeRef> {
    return mapNotNull { url ->
        val idPart = url.substringAfterLast('/', "").toIntOrNull()
        idPart?.let { EpisodeRef(id = it, url = url) }
    }
}

