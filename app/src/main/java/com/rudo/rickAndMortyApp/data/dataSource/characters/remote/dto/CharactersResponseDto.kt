package com.rudo.rickAndMortyApp.data.dataSource.characters.remote.dto

data class CharactersResponseDto(
    val info: PageDto?,
    val results: List<CharacterDto>
)
