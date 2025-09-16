package com.rudo.rickAndMortyApp.presentation.screen.characterslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.rudo.rickAndMortyApp.domain.useCase.CharactersUseCase
import com.rudo.rickAndMortyApp.domain.useCase.FavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersUseCase: CharactersUseCase,
    private val favoriteUseCase: FavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<CharactersEffect>()
    val effects: SharedFlow<CharactersEffect> = _effects.asSharedFlow()

    private val _query = MutableStateFlow("")
    private val _selectedTab = MutableStateFlow(CharacterTab.ALL)
    private val _refresh = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val pagedItems: Flow<PagingData<CharacterUi>> = combine(
        _query.debounce(300).distinctUntilChanged(),
        _selectedTab
    ) { query, tab ->
        if (tab == CharacterTab.ALL && query.isNotBlank()) {
            updateSearchingState(true)
        }
        query to tab
    }.flatMapLatest { (query, tab) ->
        if (tab == CharacterTab.ALL) {
            charactersUseCase.getCharactersStream(query.takeIf { it.isNotBlank() })
                .map { paging -> 
                    updateSearchingState(false)
                    paging.map { it.toUi() } 
                }
                .cachedIn(viewModelScope)
        } else {
            kotlinx.coroutines.flow.flowOf(PagingData.empty())
        }
    }

    val favoriteItems: Flow<List<CharacterUi>> = combine(
        _query.debounce(300).distinctUntilChanged(),
        _selectedTab,
        _refresh.onStart { emit(Unit) }
    ) { query, tab, _ ->
        if (tab == CharacterTab.FAVORITES && query.isNotBlank()) {
            updateSearchingState(true)
        }
        query to tab
    }.flatMapLatest { (query, tab) ->
        if (tab == CharacterTab.FAVORITES) {
            charactersUseCase.getFavoriteCharactersFlow()
                .map { favorites ->
                    val filtered = if (query.isNotBlank()) {
                        favorites.filter { it.name.contains(query, ignoreCase = true) }
                    } else favorites
                    updateSearchingState(false)
                    filtered.map { it.toUi() }
                }
        } else {
            kotlinx.coroutines.flow.flowOf(emptyList())
        }
    }


    private fun updateSearchingState(isSearching: Boolean) {
        _uiState.value = _uiState.value.copy(isSearching = isSearching)
    }


    fun onEvent(event: CharactersEvent) {
        when (event) {
            is CharactersEvent.OnQueryChange -> {
                _uiState.value = _uiState.value.copy(
                    query = event.value,
                    error = null
                )
                _query.value = event.value
            }

            is CharactersEvent.OnFavoriteToggle -> {
                viewModelScope.launch {
                    runCatching {
                        withContext(Dispatchers.IO) {
                            favoriteUseCase(event.id)
                        }
                    }.onFailure { e ->
                        _effects.emit(
                            CharactersEffect.ShowError(
                                e.message ?: "Error toggling favorite"
                            )
                        )
                    }
                }
            }

            is CharactersEvent.OnTabChange -> {
                _uiState.value = _uiState.value.copy(
                    selectedTab = event.tab,
                    isSearching = false
                )
                _selectedTab.value = event.tab
            }
        }
    }

    fun refresh() {
        _refresh.tryEmit(Unit)
    }

}

