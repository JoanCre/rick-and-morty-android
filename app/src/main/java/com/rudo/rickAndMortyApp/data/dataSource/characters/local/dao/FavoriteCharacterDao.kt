package com.rudo.rickAndMortyApp.data.dataSource.characters.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rudo.rickAndMortyApp.data.dataSource.characters.local.dbo.FavoriteCharacterDbo
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for favorite character IDs.
 * Follows Interface Segregation Principle - contains only methods related to favorite operations.
 */
@Dao
interface FavoriteCharacterDao {
    
    /**
     * Adds a character to favorites.
     * Uses REPLACE strategy to handle duplicates gracefully.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(favoriteCharacter: FavoriteCharacterDbo)
    
    /**
     * Removes a character from favorites by character ID.
     */
    @Query("DELETE FROM favorite_characters WHERE characterId = :characterId")
    suspend fun removeFromFavorites(characterId: Int)
    
    /**
     * Gets all favorite character IDs as a reactive Flow.
     * UI can observe this for real-time updates.
     */
    @Query("SELECT characterId FROM favorite_characters ORDER BY addedDate DESC")
    fun getFavoriteCharacterIdsFlow(): Flow<List<Int>>
    
    /**
     * Gets all favorite character IDs as a one-time list.
     * Useful for non-reactive operations.
     */
    @Query("SELECT characterId FROM favorite_characters ORDER BY addedDate DESC")
    suspend fun getFavoriteCharacterIds(): List<Int>
    
    /**
     * Checks if a character is marked as favorite.
     * Optimized query that returns boolean directly.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_characters WHERE characterId = :characterId)")
    suspend fun isFavorite(characterId: Int): Boolean
}
