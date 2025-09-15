package com.rudo.rickAndMortyApp.presentation.screen.characterslist

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.rudo.rickAndMortyApp.domain.entity.Character
import com.rudo.rickAndMortyApp.presentation.components.getSubtitleColor

data class CharactersUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val error: String? = null,
    val selectedTab: CharacterTab = CharacterTab.ALL
)

enum class CharacterTab { ALL, FAVORITES }

data class CharacterUi(
    val id: Int,
    val name: String,
    val isFavorite: Boolean,
    val image: String,
    val subtitle: AnnotatedString,
)

sealed interface CharactersEvent {
    data class OnQueryChange(val value: String) : CharactersEvent
    data class OnFavoriteToggle(val id: Int) : CharactersEvent
    data class OnTabChange(val tab: CharacterTab) : CharactersEvent
}

sealed interface CharactersEffect {
    data class ShowError(val message: String) : CharactersEffect
    data class NavigateToDetail(val characterId: Int) : CharactersEffect
}

fun Character.toUi(): CharacterUi = CharacterUi(
    id = id,
    name = name,
    isFavorite = isFavorite,
    image = image,
    subtitle = buildAnnotatedString {
        withStyle(SpanStyle(color = status.getSubtitleColor())) { append(status.name) }
        append("  ·  ")
        withStyle(SpanStyle(color = Color(0xFF6F767E))) { append(origin) }
    }
)
