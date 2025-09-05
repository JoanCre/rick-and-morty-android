package com.rudo.rickAndMortyApp.data.dataSource.characters.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for Character from Rick and Morty API.
 * Maps JSON response to Kotlin data class.
 */
data class CharacterDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("species") val species: String,
    @SerializedName("type") val type: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("origin") val origin: LocationRefDto,
    @SerializedName("location") val location: LocationRefDto,
    @SerializedName("image") val image: String,
    @SerializedName("episode") val episode: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("created") val created: String
)
