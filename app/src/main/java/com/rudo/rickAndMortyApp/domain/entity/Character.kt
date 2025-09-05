package com.rudo.rickAndMortyApp.domain.entity

data class Character(
    val id: Int,
    val name: String,
    val status: Status,
    val origin: String,
    val image: String,
    val isFavorite: Boolean = false
) {
    enum class Status { Alive, Dead, Unknown }
}
