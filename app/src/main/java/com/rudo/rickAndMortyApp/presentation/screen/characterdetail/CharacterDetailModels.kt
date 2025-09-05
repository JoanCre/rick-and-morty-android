package com.rudo.rickAndMortyApp.presentation.screen.characterdetail

data class CharacterDetailUi(
    val id: Int,
    val name: String,
    val image: String,
    val species: String,
    val type: String,
    val origin: String,
    val status: String,
    val episodes: List<EpisodeUi>,
    val isFavorite: Boolean = false
)

data class EpisodeUi(
    val id: Int,
    val code: String,
    val title: String? = null
)

data class CharacterDetailUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val detail: CharacterDetailUi? = null
)
