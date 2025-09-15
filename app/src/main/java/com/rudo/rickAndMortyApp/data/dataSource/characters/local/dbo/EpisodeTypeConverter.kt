package com.rudo.rickAndMortyApp.data.dataSource.characters.local.dbo

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

/**
 * Room TypeConverter for episode lists.
 * Converts between List<String> and JSON string for database storage.
 * Follows Single Responsibility Principle - handles only episode list conversion.
 */
class EpisodeTypeConverter {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @TypeConverter
    fun fromEpisodeList(episodes: List<String>): String {
        return json.encodeToString(episodes)
    }

    @TypeConverter
    fun toEpisodeList(episodesJson: String): List<String> {
        return try {
            json.decodeFromString<List<String>>(episodesJson)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
