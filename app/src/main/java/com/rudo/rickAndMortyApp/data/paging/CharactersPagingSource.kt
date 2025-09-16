package com.rudo.rickAndMortyApp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rudo.rickAndMortyApp.data.dataSource.characters.local.CharactersLocalDataSource
import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.CharactersRemoteDataSource
import com.rudo.rickAndMortyApp.data.dataSource.characters.remote.dto.CharacterDto
import retrofit2.HttpException
import java.io.IOException

/**
 * PagingSource responsible for providing paged Character data to Paging 3.
 *
 * Data flow:
 * - Network page is fetched via [CharactersRemoteDataSource].
 * - Local favorites are read via [CharactersLocalDataSource] (Room) to mark items as favorite.
 * - DTOs are mapped to domain [Character] to keep domain free of infrastructure concerns.
 *
 * This class is infrastructure-only: no UI/business logic here (Clean Architecture).
 */
class CharactersPagingSource(
    private val remoteDataSource: CharactersRemoteDataSource,
    private val localDataSource: CharactersLocalDataSource,
    private val searchQuery: String? = null
) : PagingSource<Int, CharacterDto>() {

    /**
     * Computes the key for a refresh operation keeping the user near the current position.
     */
    override fun getRefreshKey(state: PagingState<Int, CharacterDto>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor)
        return page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
    }

    /**
     * Loads a single page:
     * - Calls remote API with optional [searchQuery].
     * - Returns DTOs directly (mapping to domain happens in Repository layer).
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterDto> {
        val page = params.key ?: 1
        return try {
            val response = remoteDataSource.getCharacters(page, searchQuery)
            val items = response.results

            val info = response.info
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (info?.next != null) page + 1 else null

            LoadResult.Page(
                data = items,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (ioe: IOException) {
            LoadResult.Error(ioe)
        } catch (he: HttpException) {
            LoadResult.Error(he)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
