package com.rudo.rickAndMortyApp.presentation.screen.characterslist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.rudo.rickAndMortyApp.domain.entity.Character
import com.rudo.rickAndMortyApp.domain.repository.CharactersRepository
import com.rudo.rickAndMortyApp.domain.useCase.Favorite.FavoriteUseCase
import com.rudo.rickAndMortyApp.domain.useCase.GetCharactersStream.GetCharactersStreamUseCase
import com.rudo.rickAndMortyApp.domain.useCase.GetFavoriteCharacters.GetFavoriteCharactersUseCase
import com.rudo.rickAndMortyApp.presentation.components.getSubtitleColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Characters screen with optimized search functionality.
 * Uses debounce and API filtering for efficient character search.
 */
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class CharactersViewModel @Inject constructor(
    getCharactersStream: GetCharactersStreamUseCase,
    private val favoriteUseCase: FavoriteUseCase,
    private val getFavoriteCharacters: GetFavoriteCharactersUseCase,
    private val repository: CharactersRepository
) : ViewModel() {

    var uiState by mutableStateOf(CharactersUiState())
        private set

    private val _query = MutableStateFlow("")
    private val _selectedTab = MutableStateFlow(CharacterTab.ALL)
    private val _favoriteChanges = MutableStateFlow(0) // Trigger for favorites updates

    // Reactive stream that responds to query, tab, and favorite changes
    val items: Flow<PagingData<CharacterUi>> = combine(
        _query.debounce(300).distinctUntilChanged(),
        _selectedTab,
        _favoriteChanges
    ) { query, tab, _ ->
        // Reset searching state when search completes (after debounce)
        if (query.isBlank()) {
            uiState = uiState.copy(isSearching = false)
        }
        query to tab
    }.flatMapLatest { (query, tab) ->
        when (tab) {
            CharacterTab.ALL -> {
                getCharactersStream(query.takeIf { it.isNotBlank() })
                    .map { paging ->
                        // Reset searching state when data is loaded
                        uiState = uiState.copy(isSearching = false)
                        paging.map { character ->
                            val isFavorite =
                                runCatching { repository.isFavorite(character.id) }.getOrElse { false }
                            character.toUi(isFavorite)
                        }
                    }
            }

            CharacterTab.FAVORITES -> {
                createFavoritesFlow(query)
            }
        }
    }.cachedIn(viewModelScope)

    private fun createFavoritesFlow(query: String): Flow<PagingData<CharacterUi>> {
        return kotlinx.coroutines.flow.flow {
            val favorites = getFavoriteCharacters()
            val filtered = if (query.isNotBlank()) {
                favorites.filter { it.name.contains(query, ignoreCase = true) }
            } else {
                favorites
            }
            // Reset searching state when favorites data is loaded
            uiState = uiState.copy(isSearching = false)
            emit(PagingData.from(filtered.map { it.toUi(true) }))
        }
    }

    fun onEvent(event: CharactersEvent) {
        when (event) {
            is CharactersEvent.OnQueryChange -> {
                uiState = uiState.copy(
                    query = event.value,
                    isSearching = event.value.isNotBlank(),
                    error = null
                )
                _query.value = event.value
            }

            is CharactersEvent.OnFavoriteToggle -> {
                viewModelScope.launch {
                    runCatching {
                        favoriteUseCase(event.id)
                        // Trigger reactive update for both tabs
                        _favoriteChanges.value += 1
                    }.onFailure { e ->
                        uiState = uiState.copy(
                            error = e.message ?: "Error toggling favorite"
                        )
                    }
                }
            }

            is CharactersEvent.OnTabChange -> {
                uiState = uiState.copy(selectedTab = event.tab)
                _selectedTab.value = event.tab
            }
        }
    }
}

/**
 * Map domain entity into a pure UI model. All display decisions (colors/text spans) are resolved here
 * so Composables can remain stateless/dumb and simply paint data.
 */
private fun Character.toUi(isFavorite: Boolean): CharacterUi = CharacterUi(
    id = id,
    name = name,
    isFavorite = isFavorite, // Use fresh favorite state from repository
    image = image,
    subtitle = buildStatusSubtitle(status, origin)
)

/**
 * Build an AnnotatedString to color the status label while keeping origin in a neutral color.
 * In Compose, prefer AnnotatedString for inline styling instead of pushing logic into the view.
 */
private fun buildStatusSubtitle(status: Character.Status, origin: String): AnnotatedString {
    return buildAnnotatedString {
        withStyle(SpanStyle(color = status.getSubtitleColor())) { append(status.name) }
        append("  ·  ")
        withStyle(SpanStyle(color = Color(0xFF6F767E))) { append(origin) }
    }
}
