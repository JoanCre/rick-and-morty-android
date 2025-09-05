package com.rudo.rickAndMortyApp.data.dataSource.characters.local.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room DBO (Database Object) representing a favorite character ID in the local database.
 * Simplified approach - stores only character IDs to mark as favorites.
 * Follows Single Responsibility Principle - only handles favorite status persistence.
 */
@Entity(tableName = "favorite_characters")
data class FavoriteCharacterDbo(
    @PrimaryKey
    val characterId: Int,
    
    @ColumnInfo(name = "addedDate")
    val addedDate: Long = System.currentTimeMillis()
)
