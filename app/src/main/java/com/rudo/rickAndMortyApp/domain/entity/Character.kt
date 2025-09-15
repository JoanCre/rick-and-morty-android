package com.rudo.rickAndMortyApp.domain.entity

data class Character(
    val id: Int,
    val name: String,
    val status: Status,
    val species: String,
    val type: String,
    val gender: Gender,
    val origin: String,
    val location: String,
    val image: String,
    val episodes: List<String>,
    val url: String,
    val created: String,
    val isFavorite: Boolean = false
) {
    enum class Status { Alive, Dead, Unknown }
    enum class Gender { Male, Female, Genderless, Unknown }
}
