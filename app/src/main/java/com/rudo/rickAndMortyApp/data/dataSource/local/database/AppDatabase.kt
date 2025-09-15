package com.rudo.rickAndMortyApp.data.dataSource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rudo.rickAndMortyApp.data.dataSource.characters.local.dao.FavoriteCharacterDao
import com.rudo.rickAndMortyApp.data.dataSource.characters.local.dbo.EpisodeTypeConverter
import com.rudo.rickAndMortyApp.data.dataSource.characters.local.dbo.FavoriteCharacterDbo

/**
 * Room database class for the Rick and Morty app.
 * Manages database configuration and access.
 */
@Database(
    entities = [FavoriteCharacterDbo::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(EpisodeTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to favorite characters DAO.
     * Returns interface, not implementation.
     */
    abstract fun favoriteCharacterDao(): FavoriteCharacterDao

    companion object {
        const val DATABASE_NAME = "rick_morty_favorites_db"
    }
}
