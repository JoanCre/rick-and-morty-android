package com.rudo.rickAndMortyApp.data.dataSource.characters.local.dbo

import com.rudo.rickAndMortyApp.domain.entity.Character

/**
 * Extension functions for mapping between Character domain entity and FavoriteCharacterDbo.
 * Centralized mapping logic to maintain consistency across the data layer.
 */

/**
 * Convert Character domain entity to FavoriteCharacterDbo for database storage.
 */
fun Character.toDbo(): FavoriteCharacterDbo {
    return FavoriteCharacterDbo(
        id = id,
        name = name,
        status = status.name,
        species = species,
        type = type,
        gender = gender.name,
        origin = origin,
        location = location,
        image = image,
        episodes = episodes,
        url = url,
        created = created
    )
}

/**
 * Convert FavoriteCharacterDbo to Character domain entity.
 * Since this comes from favorites database, isFavorite is always true.
 */
fun FavoriteCharacterDbo.toCharacter(): Character {
    return Character(
        id = id,
        name = name,
        status = Character.Status.valueOf(status),
        species = species,
        type = type,
        gender = Character.Gender.valueOf(gender),
        origin = origin,
        location = location,
        image = image,
        episodes = episodes,
        url = url,
        created = created,
        isFavorite = true
    )
}
