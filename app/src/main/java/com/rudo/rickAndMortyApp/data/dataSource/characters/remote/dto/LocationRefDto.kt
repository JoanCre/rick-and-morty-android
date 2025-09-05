package com.rudo.rickAndMortyApp.data.dataSource.characters.remote.dto

import com.google.gson.annotations.SerializedName

data class LocationRefDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
