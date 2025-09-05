package com.rudo.rickAndMortyApp.domain.entity

data class CharacterDetail(
    val id: Int,
    val name: String,
    val status: Character.Status,
    val species: String,
    val type: String,
    val originName: String,
    val originUrl: String?,
    val image: String,
    val episodes: List<EpisodeRef>,
    val isFavorite: Boolean = false
)

/** Small value object to represent an episode reference from the character payload. */
data class EpisodeRef(
    val id: Int,
    val url: String
)
