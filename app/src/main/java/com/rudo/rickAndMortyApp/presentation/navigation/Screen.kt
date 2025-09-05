package com.rudo.rickAndMortyApp.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object MainScreen : Screen()

    @Serializable
    data class CharacterDetail(val id: Int) : Screen()
}