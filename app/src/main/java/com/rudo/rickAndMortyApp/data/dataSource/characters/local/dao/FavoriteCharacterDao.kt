package com.rudo.rickAndMortyApp.data.dataSource.characters.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rudo.rickAndMortyApp.data.dataSource.characters.local.dbo.FavoriteCharacterDbo
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for complete favorite characters.
 * Store full character data for better performance and offline support.
 * Follows Interface Segregation Principle - contains only methods related to favorite operations.
 */
@Dao
interface FavoriteCharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(favoriteCharacter: FavoriteCharacterDbo)

    @Query("DELETE FROM favorite_characters WHERE id = :characterId")
    suspend fun removeFromFavorites(characterId: Int)

    @Query("SELECT * FROM favorite_characters ORDER BY added_date DESC")
    fun getFavoriteCharactersFlow(): Flow<List<FavoriteCharacterDbo>>

    @Query("SELECT id FROM favorite_characters ORDER BY added_date DESC")
    suspend fun getFavoriteCharacterIds(): List<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_characters WHERE id = :characterId)")
    suspend fun isFavorite(characterId: Int): Boolean
}
