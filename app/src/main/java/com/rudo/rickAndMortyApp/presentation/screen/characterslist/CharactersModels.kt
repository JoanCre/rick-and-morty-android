package com.rudo.rickAndMortyApp.presentation.screen.characterslist

import androidx.compose.ui.text.AnnotatedString

/** UI state for the Characters screen. */
data class CharactersUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedTab: CharacterTab = CharacterTab.ALL
)

/** Tabs for character list filtering */
enum class CharacterTab {
    ALL,
    FAVORITES
}

/** Pure UI model for a single character item. The view should only paint this. */
data class CharacterUi(
    val id: Int,
    val name: String,
    val isFavorite: Boolean,
    val image: String,
    /** Preformatted subtitle (e.g., colored "Alive  ·  Earth"). */
    val subtitle: AnnotatedString,
)

// UI events
sealed interface CharactersEvent {
    data class OnQueryChange(val value: String) : CharactersEvent
    data class OnFavoriteToggle(val id: Int) : CharactersEvent
    data class OnTabChange(val tab: CharacterTab) : CharactersEvent
}
